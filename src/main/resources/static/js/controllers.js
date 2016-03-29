/**
 * Created by vctran on 17/03/16.
 */
angular.module('rcbook.controllers', []).controller('navigation',

    function($rootScope, $http, $location, $route) {
        var self = this;
        self.tab = function(route) {
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

        self.credentials = {};
        self.login = function() {
            authenticate(self.credentials, function(authenticated) {
                if (authenticated) {
                    console.log("Login succeeded")
                    $location.path("/home");
                    self.error = false;
                    $rootScope.authenticated = true;
                } else {
                    console.log("Login failed")
                    $location.path("/login");
                    self.error = true;
                    $rootScope.authenticated = false;
                }
            })
        };

        self.logout = function() {
            $rootScope.userStatus = '';
            $http.post('logout', {}).finally(function() {
                $rootScope.authenticated = false;
                $location.path("/");
            });
        };
        $rootScope.isNewUser = false;
        self.signup = function(credentials) {
            var newUser = {
                email: self.credentials.username,
                password: self.credentials.password
            };
            $http.post('signup', newUser).success(function(data, status, headers, config) {
                console.log("user created!");
                $rootScope.userStatus = 'User created!';
                $location.path("/login");
            });
        }
    }).controller('homeController', function($scope, $http, $location, $rootScope, Race) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    $http.get(host + '/dashboard', {params: {userId: $rootScope.user.id}}).success(function(data) {
        $scope.dashboard = data;
    });
    $scope.races = Race.query();
    $scope.go = function (path) {
        $location.path(path);
    };
    $scope.goRace = function(race) {
        console.log(race.id);
        $location.path('/racedetails/'+race.id);
    };
}).controller('profileController', function($scope, $http, $location, $rootScope) {
    $scope.user = $rootScope.user;
});