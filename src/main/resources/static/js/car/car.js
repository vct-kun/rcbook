/**
 * Created by vctran on 25/03/16.
 */
angular.module('car', []).controller('carController', function($scope, $http, Car, $rootScope, master, brands, $state) {
    $scope.master = master;
    $scope.brands = brands;
    $scope.car = new Car();
    $scope.car.user = $rootScope.user;
    $scope.addCar = function() {
        $scope.car.chassis = $scope.currentChassis;
        $scope.car.$save(function(){
            $scope.master = $scope.master.concat($scope.car);
            $scope.car = new Car();
        });
    };
    $scope.getChassis = function() {
        $http.get('getChassis', {params: {brandId :$scope.currentBrand.id}}).success(function(data){
            $scope.chassisList = data;
        });
    };
    $scope.set = function(car) {
        $state.go('main.carDetails', {car_id: car.id});
    }
}).controller('cardetailsController', function($scope, car, motors, escList, Setting, settings) {
    $scope.car = car;
    $scope.motors = motors;
    $scope.escList = escList;
    $scope.settings = settings;
    $scope.addSetup = function(car) {
        $scope.setting = new Setting();
        $scope.setting.motor = $scope.currentMotor;
        $scope.setting.esc = $scope.currentEsc;
        $scope.setting.car = car;
        $scope.setting.$save(function(){
            
        });
    };
});