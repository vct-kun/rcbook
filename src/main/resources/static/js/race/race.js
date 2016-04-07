/**
 * Created by vctran on 25/03/16.
 */
angular.module('race', []).controller('raceController', function($scope, $state, $rootScope, Race) {
    $scope.currentClub = $rootScope.ownerClub;
    $scope.race = new Race();
    $scope.race.joinedDriver = [];
    $scope.race.raceClub = $scope.currentClub;
    $scope.addRace = function() {
        $scope.race.$save(function(){
            $rootScope.ownerClub = $scope.currentClub;
            $state.go('mgtclub');
        });
    };
}).controller('racedetailsController', function($scope, $state, $rootScope, Driver, race, cars, userInRace) {
    $scope.cars = [];
    $scope.userHasJoined = false;
    $scope.noCarSelected = false;
    $scope.race = race;
    $scope.cars = cars;
    $scope.userHasJoined = userInRace;

    $scope.join = function() {
        $scope.driver = new Driver();
        $scope.driver.user = $rootScope.user;
        $scope.driver.car = $scope.currentCar;
        if ($scope.currentCar == null) {
            $scope.noCarSelected = true;
        } else {
            $scope.noCarSelected = false;
            $scope.race.joinedDriver = $scope.race.joinedDriver.concat($scope.driver);
            $scope.race.$update(function() {
                console.log('ok updating!');
                $scope.userHasJoined = true;
            });
        }
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
        var index = indexOfObject($scope.race.joinedDriver, $scope.currentDriver);
        $scope.race.joinedDriver.splice(index, 1);
        $scope.race.$update(function() {
            $scope.userHasJoined = false;
            $scope.currentDriver = '';
        });
    };

    $scope.close = function(race) {
        $state.go('closerace', {race_id:race.id});
    };
}).controller('closeRaceController', function($scope, $state, race) {
    $scope.race = race;
    $scope.close = function(race) {
        $scope.race = race;
        $scope.race.closed = true;
        $scope.race.$update(function(){
            $state.go('racedetails', {race_id:race.id});
        });
    };
});