angular.module('rcbook', [ 'ngRoute', 'ngMaterial', 'ngMessages','ngResource','rcbook.services', 'rcbook.controllers', 'car', 'club', 'race']).config(function($routeProvider, $httpProvider) {

	$routeProvider.when('/', {
		templateUrl : 'login.html',
		controller : 'navigation'//,
		//controllerAs: 'controller'
	}).when('/home', {
		templateUrl : 'home.html',
		controller : 'homeController',
		//controllerAs: 'home'//,
		resolve: {
			dashboard: function($http, $location, $rootScope) {
				var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
				return $http.get(host + '/dashboard', {params: {userId: $rootScope.user.id}}).then(function(response){
					return response.data;
				})
			},
			races: function(Race) {
				return Race.query();
			}
		}
	}).when('/newrace', {
		templateUrl : 'js/race/page_race_new.html',
		controller : 'raceController'//,
		//controllerAs: 'controller'
	}).when('/car', {
		templateUrl : 'js/car/page_car.html',
		controller : 'carController',
		//controllerAs: 'controller',
		resolve: {
			master: function($http, $location, $rootScope) {
				var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
				return $http.get(host + '/getCarByUserId', {params: {userId: $rootScope.user.id}}).then(function(response){
					return response.data;
				})
			},
			brands : function($http, $location) {
				var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
				return $http.get(host + '/getBrands').then(function(response){
					return response.data;
				})
			}
		}
	}).when('/club', {
		templateUrl : 'js/club/page_club.html',
		controller : 'clubController',
		//controllerAs: 'controller'
		resolve: {
			clubs : function(Club) {
				return Club.query();
			}
		}
	}).when('/racedetails/:race_id', {
		templateUrl : 'js/race/page_race_details.html',
		controller : 'racedetailsController'//,
		//controllerAs: 'controller'
	}).when('/adminclub', {
		templateUrl : 'js/club/page_club_new.html',
		controller : 'adminclubController'//,
		//controllerAs: 'controller'
	}).when('/clubdetails/:club_id', {
		templateUrl : 'js/club/page_club_details.html',
		controller : 'clubdetailsController'//,
		//controllerAs: 'controller'
	}).when('/mgtclub', {
		templateUrl : 'js/club/page_club_owner.html',
		controller : 'clubmgtController'//,
		//controllerAs: 'controller'
	}).when('/profile', {
		templateUrl : 'js/user/profile.html',
		controller : 'profileController'//,
		//controllerAs: 'controller'
	}).when('/closerace/:race_id', {
		templateUrl : 'js/race/page_race_close.html',
		controller : 'closeRaceController'//,
		//controllerAs: 'controller'
	}).when('/yourclub', {
		templateUrl : 'js/club/page_club.html',
		controller : 'yourclubController'//,
		//controllerAs: 'controller'
	}).otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});
