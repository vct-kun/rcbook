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
                    $rootScope.isAdmin = data.principal.user.role == 'ADMIN';
                    $rootScope.isOwner = data.principal.user.role == 'OWNER';
                    if ($rootScope.isOwner) {
                        $http.get('getOwnerClub', {params: {userId :$rootScope.user.id }}).success(function(data){
                            $rootScope.ownerClub = data;
                        });
                    }
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
    }).controller('carController', function($scope, $http, $location) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    console.log(host);
    $http.get(host + '/getCar').success(function(data) {
        $scope.master = data;
    });
    $scope.addCar = function() {
        var dataObj = {
            brand : $scope.car.brand,
            chassis : $scope.car.chassis
        };
        var res = $http.post(host + '/addCar', dataObj);
        res.success(function(data, status, headers, config) {
            $scope.master = $scope.master.concat(data);
        });
        $scope.car = '';
    };
    $http.get(host + '/getBrands').success(function(data) {
        $scope.brands = data;
    });
    $http.get(host + '/getChassis').success(function(data) {
        $scope.chassisList = data;
    });
}).controller('raceController', function($scope, $http, $location, $rootScope, Race) {
    $scope.currentClub = $rootScope.ownerClub;
    $scope.race = new Race();
    $scope.race.joinedDriver = [];
    $scope.race.raceClub = $scope.currentClub;
    $scope.addRace = function() {
        $scope.race.$save(function(){
            console.log("add race to club");
            $scope.currentClub.races = $scope.currentClub.races.concat($scope.race);
            $rootScope.ownerClub = $scope.currentClub;
            $location.path('/mgtclub');
        });
    };
}).controller('homeController', function($scope, $http, $location) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    $http.get(host + '/dashboard').success(function(data) {
        $scope.dashboard = data;
    });
    $scope.go = function (path) {
        $location.path(path);
    };
}).controller('racedetailsController', function($scope, $http, $location, $routeParams, Race, $rootScope) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    var self = this;
    self.race_id = $routeParams.race_id;
    $scope.userHasJoined = false;
    $scope.race = Race.get({id: self.race_id}, function(){
        console.log($scope.race);
        var checkUserInRace = function() {
            for (var i = 0;i<$scope.race.joinedDriver.length;i++) {
                if ($scope.race.joinedDriver[i].id == $rootScope.user.id) {
                    return true;
                }
            }
            return false;
        };
        $scope.userHasJoined = checkUserInRace();
    });

    $scope.join = function() {
        $http.get(host + '/user').success(function(data){
            console.log($rootScope.user);
            $scope.race.joinedDriver = $scope.race.joinedDriver.concat($rootScope.user);
            $scope.race.$update(function() {
                console.log('ok updating!');
                $scope.userHasJoined = true;
            });
        });
    };

    function indexOfObject(array, object) {
        for (var i=0;i<array.length;i++) {
            if (angular.equals(array[i], object)) {
                return i;
            }
        }
        return -1;
    }

    $scope.leave = function() {
        var index = indexOfObject($scope.race.joinedDriver, $rootScope.user);
        $scope.race.joinedDriver.splice(index, 1);
        $scope.race.$update(function() {
            $scope.userHasJoined = false;
        });
    };
}).controller('adminclubController', function($scope, $http, $location, Club, $rootScope) {
    $scope.club = new Club();
    $scope.club.users = [];
    $scope.club.waitingUsers = [];
    $scope.club.owner = $rootScope.user;
    $scope.addClub = function() {
        $scope.club.$save(function(){
           console.log("saving club");
            $rootScope.isOwner = true;
            $rootScope.ownerClub = $scope.club;
            $location.path('/mgtclub');
        });
    };
}).controller('clubController', function($scope, $http, $location, Club) {
    var test = Club.query(function(){
       console.log($scope.clubs);
    });
    $scope.clubs = test;
    $scope.go = function(club) {
        $location.path('/clubdetails/'+club.id);
    };
}).controller('clubdetailsController', function($scope, $http, $location, $routeParams, Club, $rootScope) {
    var self = this;
    self.club_id = $routeParams.club_id;
    $scope.club = Club.get({id: self.club_id}, function(){
        var checkUserInClub = function() {
            for (var i = 0;i<$scope.club.users.length;i++) {
                if ($scope.club.users[i].id == $rootScope.user.id) {
                    return true;
                }
            }
            return false;
        };
        $scope.userHasJoined = checkUserInClub();
    });

    $scope.join = function() {
        $scope.club.waitingUsers = $scope.club.waitingUsers.concat($rootScope.user);
        $scope.club.$update(function() {
            $scope.userHasJoined = true;
        });
    };

    function indexOfObject(array, object) {
        for (var i=0;i<array.length;i++) {
            if (angular.equals(array[i], object)) {
                return i;
            }
        }
        return -1;
    }

    $scope.leave = function() {
        var index = indexOfObject($scope.club.users, $rootScope.user);
        $scope.club.users.splice(index, 1);
        $scope.club.$update(function() {
            $scope.userHasJoined = false;
        });
    };

    $scope.approve = function(user) {
        console.log("approve user id"+user.id);
        $scope.club.users = $scope.club.users.concat(user);
        var index = indexOfObject($scope.club.waitingUsers, user);
        $scope.club.waitingUsers.splice(index, 1);
        $scope.club.$update(function() {

        });
    };

    $scope.decline = function(user) {
        console.log("decline user id"+user.id);
        var index = indexOfObject($scope.club.waitingUsers, user);
        $scope.club.waitingUsers.splice(index, 1);
        $scope.club.$update();
    };

    $scope.go = function(race) {
        console.log(race.id);
        $location.path('/racedetails/'+race.id);
    };

}).controller('clubmgtController', function($scope, $http, $location, Club, $rootScope) {
    $scope.currentClub = $rootScope.ownerClub;
    $scope.goToRace = function() {
      $location.path('/newrace');
    };
    $scope.go = function(race) {
        console.log(race.id);
        $location.path('/racedetails/'+race.id);
    };
}).controller('profileController', function($scope, $http, $location, $rootScope) {
    $scope.user = $rootScope.user;
});