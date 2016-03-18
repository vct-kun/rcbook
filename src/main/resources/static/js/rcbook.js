angular.module('rcbook', [ 'ngRoute', 'ngMaterial', 'ngMessages','ngResource','rcbook.services', 'rcbook.controllers']).config(function($routeProvider, $httpProvider) {

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
		templateUrl : 'page_club.html',
		controller : 'clubController',
		controllerAs: 'controller'
	}).when('/racedetails/:race_id', {
		templateUrl : 'racedetails.html',
		controller : 'racedetailsController',
		controllerAs: 'controller'
	}).when('/adminclub', {
		templateUrl : 'adminclub.html',
		controller : 'adminclubController',
		controllerAs: 'controller'
	}).otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});
