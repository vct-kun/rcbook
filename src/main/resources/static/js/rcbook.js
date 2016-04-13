angular.module('rcbook', [ 'ngRoute', 'ngMaterial', 'ngMessages','ngResource','rcbook.services', 'rcbook.controllers', 'car', 'club', 'race', 'angular-storage', 'ui.router', 'satellizer', 'profile']).config(function($routeProvider, $httpProvider, $stateProvider, $authProvider, $urlRouterProvider) {

	$urlRouterProvider.otherwise('/');

	$stateProvider
		.state('login', {
			url: '/',
			templateUrl: 'login.html',
			controller: 'loginController'
		})
		.state('home', {
			url: '/home',
			templateUrl: 'home.html',
			controller: 'homeController',
			resolve: {
				redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
				dashboard: function($http, $auth) {
					return $http.get('dashboard', {params: {userId:$auth.getPayload().sub}}).then(function(response){
						return response.data;
					})
				},
				races: function(Race) {
					return Race.query();
				}
			}
		})
		.state('newrace', {
			url: '/newrace',
			templateUrl : 'js/race/page_race_new.html',
			controller : 'raceController',
			resolve: {
				redirectIfNotAuthenticated: _redirectIfNotAuthenticated
			}
		})
		.state('car', {
			url: '/car',
			templateUrl : 'js/car/page_car.html',
			controller : 'carController',
			resolve: {
					redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
					master: function($http, $auth) {
						return $http.get('getCarByUserId', {params: {userId:$auth.getPayload().sub}}).then(function(response){
							return response.data;
						})
					},
					brands : function($http) {
						return $http.get('getBrands').then(function(response){
							return response.data;
						})
					}
				}
		})
		.state('club', {
			url: '/club',
			templateUrl : 'js/club/page_club.html',
			controller : 'clubController',
			resolve: {
				redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
				clubs : function(Club) {
					return Club.query();
				}
			}
		})
		.state('racedetails', {
			url: '/racedetails/:race_id',
			templateUrl : 'js/race/page_race_details.html',
			controller : 'racedetailsController',
			resolve: {
				redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
				race: function (Race, $stateParams) {
					return Race.get({id: $stateParams.race_id});
				},
				userInRace: function ($http, $rootScope, $stateParams) {
					return $http.get('isUserInRace/' + $stateParams.race_id + '/' + $rootScope.user.id).then(function (response) {
						return response.data;
					})
				},
				cars: function ($http, $rootScope) {
					return $http.get('getCarByUserId', {params: {userId: $rootScope.user.id}}).then(function (response) {
						return response.data;
					})
				}
			}
		})
		.state('adminclub', {
			url: '/adminclub',
			templateUrl : 'js/club/page_club_new.html',
			controller : 'adminclubController',
			resolve: {
				redirectIfNotAuthenticated: _redirectIfNotAuthenticated
			}
		})
		.state('clubdetails', {
			url: '/clubdetails/:club_id',
			templateUrl : 'js/club/page_club_details.html',
			controller : 'clubdetailsController',
			resolve: {
				redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
				club : function(Club, $stateParams) {
					return Club.get({id: $stateParams.club_id});
				},
				races: function($http, $stateParams) {
					return $http.get('getRacesByClub', {params: {clubId: $stateParams.club_id}}).then(function(response){
						return response.data;
					})
				}
			}
		})
		.state('mgtclub', {
			url: '/mgtclub',
			templateUrl : 'js/club/page_club_owner.html',
			controller : 'clubmgtController',
			resolve : {
				redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
				currentClub : function(Club, $rootScope) {
					return Club.get({id:$rootScope.ownerClub.id});
				},
				races: function($http, $rootScope) {
					return $http.get('getRacesByClub', {params: {clubId: $rootScope.ownerClub.id}}).then(function (response) {
						return response.data;
					})
				}
			}
		})
		.state('profile', {
			url: '/profile',
			templateUrl : 'js/user/profile.html',
			controller : 'profileController',
			resolve: {
				redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
				profile: function($http, $auth, $rootScope) {
					return $http.get('user/' + $auth.getPayload().sub).then(function(response){
						$rootScope.authenticated = true;
						$rootScope.isOwner = $rootScope.user.isOwner;
						$rootScope.haveClub = $rootScope.user.userHasClub;
						return response.data;
					});
				}
			}
		})
		.state('closerace', {
			url: '/closerace/:race_id',
			templateUrl : 'js/race/page_race_close.html',
			controller : 'closeRaceController',
			resolve: {
				redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
				race: function(Race, $stateParams) {
					return Race.get({id: $stateParams.race_id});
				}
			}
		})
		.state('yourclub', {
			url: '/yourclub',
			templateUrl : 'js/club/page_club.html',
			controller : 'yourclubController',
			resolve: {
				redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
				clubs: function($http, $rootScope) {
					return $http.get('getClubsByUserId/'+$rootScope.user.id).then(function (response) {
						return response.data;
					})
				}
			}
		})
		.state('payment', {
			url: '/payment',
			templateUrl: 'js/payment.html',
			controller: 'paymentController'
		});


	function _redirectIfNotAuthenticated($q, $state, $auth) {
		var defer = $q.defer();
		if($auth.isAuthenticated()) {
			defer.resolve();
		} else {
			$timeout(function () {
				console.log('not authenticated!');
				$state.go('login');
			});
			defer.reject();
		}
		return defer.promise;
	}

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	$httpProvider.interceptors.push('loginInterceptor');
});
