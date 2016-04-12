/**
 * Created by vctran on 12/04/16.
 */
angular.module('profile', []).controller('profileController', function($scope, $rootScope, $http, $window) {
    $scope.user = $rootScope.user;
    $scope.pay = function() {
        $http.get('payment').success(function(data) {
            console.log(data);
            $window.location.href = data.url;
        });
    }
});