/**
 * Created by vctran on 17/03/16.
 */
angular.module('rcbook.controllers', []).controller('homeController', function($scope, $state, dashboard, races, $auth, $rootScope, $http) {
    if ($auth.isAuthenticated()) {
        $rootScope.authenticated = true;
        $http.get('user/' + $auth.getPayload().sub).then(function(response){
            $rootScope.user = response.data;
            $rootScope.isOwner = $rootScope.user.isOwner;
            $rootScope.haveClub = $rootScope.user.userHasClub;
            if ($rootScope.isOwner) {
                $http.get('getOwnerClub', {params: {userId :$rootScope.user.id }}).success(function(data){
                    $rootScope.ownerClub = data;
                });
            }
        });
    }
    $scope.dashboard = dashboard;
    $scope.races = races;
    $scope.goRace = function(race) {
        $state.go('racedetails', {race_id:race.id});
    };
}).controller('mainController', function($rootScope, $state, $auth, $scope){
    $scope.logout = function() {
        $auth.logout();
        $rootScope.authenticated = false;
        $state.go("login");
    };
}).controller('loginController', function($scope, $auth, $rootScope, $state){
    $rootScope.isNewUser = false;
    $scope.credentials = {};
    $scope.login = function() {
        var user = {
            email: $scope.credentials.username,
            password: $scope.credentials.password
        };
        $auth.login(user).then(function (response) {
            $auth.setToken(response);
            $rootScope.authenticated = true;
            $state.go('home');
        });
    };
}).controller('paymentController', function($state){
    $state.go('home');
});