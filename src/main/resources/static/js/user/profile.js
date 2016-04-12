/**
 * Created by vctran on 12/04/16.
 */
angular.module('profile', []).controller('profileController', function($scope, profile, $http, $window) {
    $scope.user = profile;
    $scope.isFree = $scope.user.account == 'FREE';
    $scope.pay = function() {
        $http.get('payment').success(function(data) {
            console.log(data);
            $window.location.href = data.url;
        });
    }
});