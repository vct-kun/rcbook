/**
 * Created by vctran on 17/03/16.
 */
angular.module('rcbook.controllers', []).controller('homeController', function($scope, $state, dashboard, races) {
    $scope.dashboard = dashboard;
    $scope.races = races;
    $scope.goRace = function(race) {
        $state.go('main.racedetails', {race_id:race.id});
    };
}).controller('navbarController', function($rootScope, $state, $auth, $scope){
    $scope.$on('$stateChangeSuccess', function() {
        $scope.authenticated = $rootScope.authenticated;
    });
    $scope.logout = function() {
        $auth.logout();
        $rootScope.authenticated = false;
        $state.go("main");
    };
}).controller('loginController', function($scope, $auth, $rootScope, $state, $http){
    $scope.credentials = {};
    $scope.login = function() {
        var user = {
            email: $scope.credentials.username,
            password: $scope.credentials.password
        };
        $auth.login(user)
            .then(function (response) {
                $auth.setToken(response);
                $rootScope.authenticated = true;
                $http.get('user/' + $auth.getPayload().sub).then(function(response){
                    $rootScope.user = response.data;
                    $rootScope.isOwner = $rootScope.user.owner;
                    $rootScope.haveClub = $rootScope.user.userHasClub;
                    $rootScope.isPremium = $rootScope.user.premium;
                    if ($rootScope.isOwner) {
                        $http.get('getOwnerClub', {params: {userId :$rootScope.user.id }}).success(function(data){
                            $rootScope.ownerClub = data;
                        });
                    }
                    $state.go('main.home', {}, {notify:true});
                });
            })
            .catch(function(response){
                $scope.error = true;
            });
    };
}).controller('sidebarController', function($scope, $rootScope){
    $scope.$on('$stateChangeSuccess', function() {
        $scope.authenticated = $rootScope.authenticated;
        $scope.isPremium = $rootScope.isPremium;
        $scope.isOwner = $rootScope.isOwner;
        $scope.haveClub = $rootScope.haveClub;
    });
}).controller('signupController', function($scope, $auth, $timeout, $state){
    $scope.credentials = {};
    $scope.accountCreated = false;
    $scope.signup = function() {
        var user = {
            email: $scope.credentials.username,
            password: $scope.credentials.password
        };
        $auth.signup(user)
            .then(function (response) {
                $scope.accountCreated = true;
                $timeout(function(){
                    $state.go('main');
                }, 5000);
            })
            .catch(function (response) {

            });
    }
});