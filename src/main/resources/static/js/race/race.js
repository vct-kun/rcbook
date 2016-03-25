/**
 * Created by vctran on 25/03/16.
 */
angular.module('race', []).controller('raceController', function($scope, $http, $location, $rootScope, Race) {
    $scope.currentClub = $rootScope.ownerClub;
    $scope.race = new Race();
    $scope.race.joinedDriver = [];
    $scope.race.raceClub = $scope.currentClub;
    $scope.addRace = function() {
        $scope.race.$save(function(){
            console.log("add race to club");
            //$scope.currentClub.races = $scope.currentClub.races.concat($scope.race);
            $rootScope.ownerClub = $scope.currentClub;
            $location.path('/mgtclub');
        });
    };
}).controller('racedetailsController', function($scope, $http, $location, $routeParams, Race, $rootScope, Driver) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    var self = this;
    self.race_id = $routeParams.race_id;
    $scope.userHasJoined = false;
    $scope.race = Race.get({id: self.race_id}, function(){
        console.log($scope.race);
        var checkUserInRace = function() {
            for (var i = 0;i<$scope.race.joinedDriver.length;i++) {
                if ($scope.race.joinedDriver[i].user.id == $rootScope.user.id) {
                    $scope.currentDriver = $scope.race.joinedDriver[i];
                    return true;
                }
            }
            return false;
        };
        $scope.userHasJoined = checkUserInRace();
    });

    $scope.join = function() {
        $scope.driver = new Driver();
        $scope.driver.user = $rootScope.user;
        $scope.driver.car = $scope.currentCar;
        $scope.driver.$save(function(){
            $scope.race.joinedDriver = $scope.race.joinedDriver.concat($scope.driver);
            $scope.race.$update(function() {
                console.log('ok updating!');
                $scope.userHasJoined = true;
            });
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
        var index = indexOfObject($scope.race.joinedDriver, $scope.currentDriver);
        $scope.race.joinedDriver.splice(index, 1);
        $scope.race.$update(function() {
            $scope.userHasJoined = false;
            $scope.currentDriver = '';
        });
    };

    $http.get(host + '/getCarByUserId', {params : {userId: $rootScope.user.id}}).success(function(data){
        $scope.cars = data;
    });
    $scope.close = function(race) {
        $location.path('/closerace/'+race.id);
    };
}).controller('closeRaceController', function($scope, $http, $location, $rootScope, $routeParams, Race) {
    $scope.race = Race.get({id: $routeParams.race_id});
});