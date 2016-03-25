/**
 * Created by vctran on 25/03/16.
 */
angular.module('car', []).controller('carController', function($scope, $http, $location, Car, $rootScope) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    $http.get(host + '/getCarByUserId', {params: {userId: $rootScope.user.id}}).success(function(data) {
        $scope.master = data;
    });
    $scope.car = new Car();
    $scope.car.user = $rootScope.user;
    $scope.addCar = function() {
        $scope.car.chassis = $scope.currentChassis;
        $scope.car.$save(function(){
            $scope.master = $scope.master.concat($scope.car);
            $scope.car = new Car();
        });
    };
    $http.get(host + '/getBrands').success(function(data) {
        $scope.brands = data;
    });
    $scope.getChassis = function() {
        $http.get(host + '/getChassis', {params: {brandId :$scope.currentBrand.id}}).success(function(data){
            $scope.chassisList = data;
        });
    };
});