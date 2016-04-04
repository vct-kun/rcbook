/**
 * Created by vctran on 17/03/16.
 */
angular.module('rcbook.controllers', []).controller('navigation',

    function($rootScope, $http, $location, $route, $scope, UserService) {
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
                    UserService.setCurrentUser(data.principal.user);
                    $rootScope.$broadcast('authorized');
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
    }).controller('homeController', function($scope, $location, dashboard, races) {
    $scope.dashboard = dashboard;
    $scope.races = races;
    $scope.go = function (path) {
        $location.path(path);
    };
    $scope.goRace = function(race) {
        $location.path('/racedetails/'+race.id);
    };
}).controller('profileController', function($scope, $http, $location, $rootScope) {
    $scope.user = $rootScope.user;
}).controller('mainController', function($rootScope, UserService){
    var main = this;
    $rootScope.$on('authorized', function() {
        main.currentUser = UserService.getCurrentUser();
        $rootScope.authenticated = true;
    });

    $rootScope.$on('unauthorized', function() {
        main.currentUser = UserService.setCurrentUser(null);
        //$state.go('login');
        $location.go('/');
    });

    main.currentUser = UserService.getCurrentUser();
});