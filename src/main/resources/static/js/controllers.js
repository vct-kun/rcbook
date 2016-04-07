/**
 * Created by vctran on 17/03/16.
 */
angular.module('rcbook.controllers', []).controller('navigation',

    function($rootScope, $http, $location, $route, $scope) {
        //var self = this;
        $scope.tab = function(route) {
            return $route.current && route === $route.current.controller;
        };
        var authenticate = function(credentials, callback) {
            var headers = credentials ? {
                authorization : "Basic "
                + btoa(credentials.username + ":"
                    + credentials.password)
            } : {};
            $http.get('user', {
                headers : headers
            }).success(function(data) {
                if (data.name) {
                    $rootScope.authenticated = true;
                    $rootScope.user = data.principal.user;
                    $rootScope.isOwner = data.principal.user.role == 'OWNER';
                    if ($rootScope.isOwner) {
                        $http.get('getOwnerClub', {params: {userId :$rootScope.user.id }}).success(function(data){
                            $rootScope.ownerClub = data;
                        });
                    }
                    $http.get('userHasClub', {params: {userId :$rootScope.user.id }}).success(function(data) {
                        $rootScope.haveClub = data;
                    });
                } else {
                    $rootScope.authenticated = false;
                }
                callback && callback($rootScope.authenticated);
            }).error(function() {
                $rootScope.authenticated = false;
                callback && callback(false);
            });
        };

        $scope.credentials = {};
        $scope.login = function() {
            authenticate($scope.credentials, function(authenticated) {
                if (authenticated) {
                    console.log("Login succeeded")
                    $location.path("/home");
                    $scope.error = false;
                    $rootScope.authenticated = true;
                } else {
                    console.log("Login failed")
                    $location.path("/login");
                    $scope.error = true;
                    $rootScope.authenticated = false;
                }
            })
        };

        $scope.logout = function() {
            $rootScope.userStatus = '';
            $http.post('logout', {}).finally(function() {
                $rootScope.authenticated = false;
                $location.path("/");
            });
        };
        $rootScope.isNewUser = false;
        $scope.signup = function(credentials) {
            var newUser = {
                email: $scope.credentials.username,
                password: $scope.credentials.password
            };
            $http.post('signup', newUser).success(function(data, status, headers, config) {
                console.log("user created!");
                $rootScope.userStatus = 'User created!';
                $location.path("/login");
            });
        }
    }).controller('homeController', function($scope, $state, dashboard, races, $auth, $rootScope, $http) {
    console.log("Authenticated"+$auth.isAuthenticated());
    if ($auth.isAuthenticated()) {
        $rootScope.authenticated = true;
        $http.get('user/' + $auth.getPayload().sub).then(function(response){
            $rootScope.user = response.data;
            $rootScope.isOwner = $rootScope.user.role == 'OWNER';
            if ($rootScope.isOwner) {
                $http.get('getOwnerClub', {params: {userId :$rootScope.user.id }}).success(function(data){
                    $rootScope.ownerClub = data;
                });
            }
            $http.get('userHasClub', {params: {userId :$rootScope.user.id }}).success(function(data) {
                $rootScope.haveClub = data;
            });
        });
    }
    $scope.dashboard = dashboard;
    $scope.races = races;
    $scope.goRace = function(race) {
        $state.go('racedetails', {race_id:race.id});
    };
}).controller('profileController', function($scope, $rootScope) {
    $scope.user = $rootScope.user;
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