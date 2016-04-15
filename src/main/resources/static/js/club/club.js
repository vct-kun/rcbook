/**
 * Created by vctran on 25/03/16.
 */
angular.module('club', []).controller('adminclubController', function($scope, $state, Club, $rootScope) {
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
            $state.go('main.mgtclub');
        });
    };
}).controller('clubController', function($scope, clubs, $state) {
    $scope.clubs = clubs;
    $scope.go = function(club) {
        $state.go('main.clubdetails', {club_id: club.id});
    };
}).controller('clubdetailsController', function($scope, $state, Club, $rootScope, club, races, userInClub) {
    $scope.club = club;
    $scope.races = races;
    $scope.userHasJoined = userInClub;

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
        $state.go('main.racedetails', {race_id: race.id});
    };

}).controller('clubmgtController', function($scope, $state, Club, currentClub, races) {
    $scope.currentClub = currentClub;
    $scope.races = races;
    $scope.goToRace = function() {
        $state.go('main.newrace');
    };
    $scope.go = function(race) {
        $state.go('main.racedetails/', {race_id:race.id});
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
}).controller('yourclubController', function($scope, $state, clubs) {
    $scope.clubs = clubs;
    $scope.go = function(club) {
        $state.go('main.clubdetails', {club_id:club.id});
    };
});