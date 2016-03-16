angular.module('hello', [ 'ngRoute', 'ngMaterial', 'ngMessages']).config(function($routeProvider, $httpProvider) {

	$routeProvider.when('/', {
		templateUrl : 'login.html',
		controller : 'navigation',
		controllerAs: 'controller'
	}).when('/home', {
		templateUrl : 'home.html',
		controller : 'homeController',
		controllerAs: 'controller'
	}).when('/race', {
		templateUrl : 'race.html',
		controller : 'raceController',
		controllerAs: 'controller'
	}).when('/car', {
		templateUrl : 'car.html',
		controller : 'carController',
		controllerAs: 'controller'
	}).when('/club', {
		templateUrl : 'club.html',
		controller : 'clubController',
		controllerAs: 'controller'
	}).otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
}).controller('navigation',

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
}).controller('raceController', function($scope, $http, $location) {
	var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
	$http.get(host + '/getRace').success(function(data) {
		$scope.master = data;
	});
	$scope.myDate = new Date();
	$scope.addRace = function() {
		var dataObj = {
			startDate: $scope.myDate,
			nbDriver : $scope.currentRace.nbDriver
		};
		var res = $http.post(host + '/addRace', dataObj);
		res.success(function(data, status, headers, config) {
			$scope.master = $scope.master.concat(data);
		});
		$scope.currentRace = '';
	};
}).controller('homeController', function($scope, $http, $location) {
	var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
	$http.get(host + '/dashboard').success(function(data) {
		$scope.dashboard = data;
	});
});
