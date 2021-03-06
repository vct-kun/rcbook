/**
 * Created by vctran on 12/04/16.
 */
angular.module('profile', []).controller('profileController', function($scope, profile, $http, $window, $state) {
    $scope.user = profile;
    $scope.isPremium = $scope.user.premium;
    $scope.isSuspended = ($scope.user.account == 'FREE' && $scope.user.agreementId != null);
    $scope.subscribe = function() {
        $http.get('subscription').success(function(data) {
            $window.location.href = data.url;
        });
    };
    $scope.suspend = function() {
        $http.get('suspend', {params:{userId:$scope.user.id}}).success(function() {
            $state.reload();
        });
    };
    $scope.reactivate = function() {
        $http.get('reactivate', {params:{userId:$scope.user.id}}).success(function() {
            $state.reload();
        });
    };
});