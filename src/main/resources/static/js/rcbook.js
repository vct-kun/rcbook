angular.module('rcbook', [ 'ngRoute', 'ngMaterial', 'ngMessages','ngResource','rcbook.services', 'rcbook.controllers']).config(function($routeProvider, $httpProvider) {

	$routeProvider.when('/', {
		templateUrl : 'login.html',
		controller : 'navigation',
		controllerAs: 'controller'
	}).when('/home', {
		templateUrl : 'home.html',
		controller : 'homeController',
		controllerAs: 'controller'
	}).when('/newrace', {
		templateUrl : 'js/race/page_race_new.html',
		controller : 'raceController',
		controllerAs: 'controller'
	}).when('/car', {
		templateUrl : 'js/car/page_car.html',
		controller : 'carController',
		controllerAs: 'controller'
	}).when('/club', {
		templateUrl : 'js/club/page_club.html',
		controller : 'clubController',
		controllerAs: 'controller'
	}).when('/racedetails/:race_id', {
		templateUrl : 'js/race/page_race_details.html',
		controller : 'racedetailsController',
		controllerAs: 'controller'
	}).when('/adminclub', {
		templateUrl : 'js/club/page_club_new.html',
		controller : 'adminclubController',
		controllerAs: 'controller'
	}).when('/clubdetails/:club_id', {
		templateUrl : 'js/club/page_club_details.html',
		controller : 'clubdetailsController',
		controllerAs: 'controller'
	}).when('/mgtclub', {
		templateUrl : 'js/club/page_club_owner.html',
		controller : 'clubmgtController',
		controllerAs: 'controller'
	}).when('/profile', {
		templateUrl : 'js/user/profile.html',
		controller : 'profileController',
		controllerAs: 'controller'
	}).when('/closerace/:race_id', {
		templateUrl : 'js/race/page_race_close.html',
		controller : 'closeRaceController',
		controllerAs: 'controller'
	}).otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});
