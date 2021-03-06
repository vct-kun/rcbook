/**
 * Created by vctran on 25/03/16.
 */
angular.module('club', []).controller('adminclubController', function($scope, $state, Club, $rootScope, Upload) {
    $scope.club = new Club();
    $scope.club.users = [];
    $scope.club.waitingUsers = [];
    $scope.club.owner = $rootScope.user;
    $scope.club.users = $scope.club.users.concat($rootScope.user);
    $scope.addClub = function(file) {
        var test = Upload.upload({
           url: 'upload',
            data: {file: file, userId: $rootScope.user.id}
        });
        test.then(function (response) {
            console.log(response.data);
            $scope.club.logoUrl = response.data.url;

            $scope.club.$save(function(){
                $rootScope.isOwner = true;
                $rootScope.ownerClub = $scope.club;
                $state.go('main.mgtclub');
            });
        });
    };
}).controller('clubController', function($scope, clubs, $state) {
    $scope.clubs = clubs;
    $scope.go = function(club) {
        $state.go('main.clubdetails', {club_id: club.id});
    };
}).controller('clubdetailsController', function($scope, $state, Club, $rootScope, club, races, userInClub, trainings) {
    $scope.club = club;
    $scope.races = races;
    $scope.userHasJoined = userInClub;
    $scope.trainings = trainings;
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
        $state.go('main.racedetails', {race_id:race.id});
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
        $scope.currentClub.users = $scope.currentClub.users.concat(user);
        var index = indexOfObject($scope.currentClub.waitingUsers, user);
        $scope.currentClub.waitingUsers.splice(index, 1);
        $scope.currentClub.$update();
    };

    $scope.decline = function(user) {
        var index = indexOfObject($scope.currentClub.waitingUsers, user);
        $scope.currentClub.waitingUsers.splice(index, 1);
        $scope.currentClub.$update();
    };

    $scope.goToTraining = function(club) {
        $state.go('main.clubtraining', {club_id:club.id});
    };
}).controller('yourclubController', function($scope, $state, clubs) {
    $scope.clubs = clubs;
    $scope.go = function(club) {
        $state.go('main.clubdetails', {club_id:club.id});
    };
}).controller('clubTrainingController', function($scope, Training, currentClub, $state) {
    $scope.currentClub = currentClub;
    $scope.addTraining = function() {
        $scope.training = new Training();
        $scope.training.date = $scope.currentDate;
        $scope.training.startTime = $scope.startTime;
        $scope.training.endTime = $scope.endTime;
        $scope.training.track = $scope.track;
        $scope.training.town = $scope.town;
        $scope.training.club = $scope.currentClub;
        $scope.training.$save(function(){
            $state.go('main.clubdetails', {club_id:$scope.currentClub.id});
        });
    };
});