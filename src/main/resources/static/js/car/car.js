/**
 * Created by vctran on 25/03/16.
 */
angular.module('car', []).controller('carController', function($scope, $http, $location, Car, $rootScope, master, brands) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
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
        $http.get(host + '/getChassis', {params: {brandId :$scope.currentBrand.id}}).success(function(data){
            $scope.chassisList = data;
        });
    };
});