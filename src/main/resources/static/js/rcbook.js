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
		templateUrl : 'page_race_new.html',
		controller : 'raceController',
		controllerAs: 'controller'
	}).when('/car', {
		templateUrl : 'car.html',
		controller : 'carController',
		controllerAs: 'controller'
	}).when('/club', {
		templateUrl : 'page_club.html',
		controller : 'clubController',
		controllerAs: 'controller'
	}).when('/racedetails/:race_id', {
		templateUrl : 'page_race_details.html',
		controller : 'racedetailsController',
		controllerAs: 'controller'
	}).when('/adminclub', {
		templateUrl : 'page_club_new.html',
		controller : 'adminclubController',
		controllerAs: 'controller'
	}).when('/clubdetails/:club_id', {
		templateUrl : 'page_club_details.html',
		controller : 'clubdetailsController',
		controllerAs: 'controller'
	}).when('/mgtclub', {
		templateUrl : 'page_club_owner.html',
		controller : 'clubmgtController',
		controllerAs: 'controller'
	}).otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});
