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
            $state.go('main.mgtclub');
        });
    };
}).controller('racedetailsController', function($scope, $state, $rootScope, Driver, race, cars, userInRace, $http) {
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
        $scope.driver.setting = $scope.currentSetting;
        if ($scope.race.haveFees == true) {
            $scope.driver.joiningStatus = 'WAITING';
        }
        if ($scope.currentCar == null || $scope.currentSetting == null) {
            if ($scope.currentCar == null) {
                $scope.noCarSelected = true;
            } else {
                $scope.noCarSelected = false;
            }
            if ($scope.currentSetting == null) {
                $scope.noSettingSelected = true;
            }
        } else {
            $scope.noCarSelected = false;
            $scope.noSettingSelected = false;
            $scope.race.joinedDriver = $scope.race.joinedDriver.concat($scope.driver);
            $scope.race.$update(function() {
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
        $state.go('main.closerace', {race_id:race.id});
    };

    $scope.getSettings = function() {
        $http.get('getSettingsByCarId/' + $scope.currentCar.id).success(function(data){
            $scope.settings = data;
        });
    };
}).controller('closeRaceController', function($scope, $state, race) {
    $scope.race = race;
    $scope.close = function(race) {
        $scope.race = race;
        $scope.race.closed = true;
        $scope.race.$update(function(){
            $state.go('main.racedetails', {race_id:race.id});
        });
    };
});