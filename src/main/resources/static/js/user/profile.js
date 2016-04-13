/**
 * Created by vctran on 12/04/16.
 */
angular.module('profile', []).controller('profileController', function($scope, profile, $http, $window) {
    $scope.user = profile;
    $scope.isPremium = $scope.user.isPremium;
    $scope.pay = function() {
        $http.get('payment').success(function(data) {
            console.log(data);
            $window.location.href = data.url;
        });
    };
    $scope.subscribe = function() {
        $http.get('subscription').success(function(data) {
            console.log(data);
            $window.location.href = data.url;
        });
    };
});