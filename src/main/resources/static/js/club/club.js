/**
 * Created by vctran on 25/03/16.
 */
angular.module('club', []).controller('adminclubController', function($scope, $http, $location, Club, $rootScope) {
    $scope.club = new Club();
    $scope.club.users = [];
    $scope.club.waitingUsers = [];
    $scope.club.owner = $rootScope.user;
    $scope.club.users = $scope.club.users.concat($rootScope.user);
    $scope.addClub = function() {
        $scope.club.$save(function(){
            console.log("saving club");
            $rootScope.isOwner = true;
            $rootScope.ownerClub = $scope.club;
            $location.path('/mgtclub');
        });
    };
}).controller('clubController', function($scope, $location, clubs) {
    $scope.clubs = clubs;
    $scope.go = function(club) {
        $location.path('/clubdetails/'+club.id);
    };
}).controller('clubdetailsController', function($scope, $location, Club, $rootScope, club, races) {
    $scope.club = club;
    $scope.races = races;

    function checkUserInClub(array) {
        for (var i = 0;i<array.length;i++) {
            if (array[i].id == $rootScope.user.id) {
                return true;
            }
        }
        return false;
    }

    $scope.userHasJoined = checkUserInClub($scope.club.users);

    $scope.join = function() {
        $scope.club.waitingUsers = $scope.club.waitingUsers.concat($rootScope.user);
        $scope.club.$update(function() {
            $scope.userHasJoined = true;
        });
    };

    function indexOfObject(array, object) {
        for (var i=0;i<array.length;i++) {
            if (angular.equals(array[i], object)) {
                return i;
            }
        }
        return -1;
    }

    $scope.leave = function() {
        var index = indexOfObject($scope.club.users, $rootScope.user);
        $scope.club.users.splice(index, 1);
        $scope.club.$update(function() {
            $scope.userHasJoined = false;
        });
    };

    $scope.go = function(race) {
        $location.path('/racedetails/'+race.id);
    };

}).controller('clubmgtController', function($scope, $http, $location, Club, $rootScope) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    $scope.currentClub = Club.get({id:$rootScope.ownerClub.id}, function(){

    });
    $http.get(host + '/getRacesByClub', {params: {clubId: $rootScope.ownerClub.id}}).success(function(data){
        $scope.races = data;
    });
    $scope.goToRace = function() {
        $location.path('/newrace');
    };
    $scope.go = function(race) {
        console.log(race.id);
        $location.path('/racedetails/'+race.id);
    };
    function indexOfObject(array, object) {
        for (var i=0;i<array.length;i++) {
            if (angular.equals(array[i], object)) {
                return i;
            }
        }
        return -1;
    }
    $scope.approve = function(user) {
        console.log("approve user id"+user.id);
        $scope.currentClub.users = $scope.currentClub.users.concat(user);
        var index = indexOfObject($scope.currentClub.waitingUsers, user);
        $scope.currentClub.waitingUsers.splice(index, 1);
        $scope.currentClub.$update(function() {

        });
    };

    $scope.decline = function(user) {
        console.log("decline user id"+user.id);
        var index = indexOfObject($scope.currentClub.waitingUsers, user);
        $scope.currentClub.waitingUsers.splice(index, 1);
        $scope.currentClub.$update();
    };
}).controller('yourclubController', function($scope, $http, $location, $rootScope) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    $http.get(host + '/getClubsByUserId/'+$rootScope.user.id, {}).success(function(data){
        $scope.clubs = data;
    });
    $scope.go = function(club) {
        $location.path('/clubdetails/'+club.id);
    };
});