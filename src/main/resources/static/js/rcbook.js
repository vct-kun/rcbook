angular.module('rcbook', [ 'ngRoute', 'ngMaterial', 'ngMessages','ngResource','rcbook.services', 'rcbook.controllers', 'car', 'club', 'race', 'ui.router', 'satellizer', 'profile', 'ngFileUpload', 'payment']).config(function($httpProvider, $stateProvider, $authProvider, $urlRouterProvider) {

	$urlRouterProvider.otherwise('/');

	$stateProvider
		.state('main', {
			url: '/',
			views: {
				'navbar': {
					templateUrl: 'js/navbar.html',
					controller: 'navbarController'
				},
				'sidebar':{
					templateUrl: 'js/sidebar.html',
					controller: 'sidebarController'
				},
				'content': {
					templateUrl: 'login.html',
					controller: 'loginController'
				}
			}
		})
		.state('main.home', {
			url: 'home',
			views: {
				'content@': {
					templateUrl: 'home.html',
					controller: 'homeController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
						dashboard: function ($http, $auth) {
							return $http.get('dashboard', {params: {userId: $auth.getPayload().sub}}).then(function (response) {
								return response.data;
							})
						},
						races: function (Race) {
							return Race.query();
						}
					}
				}
			}
		})
		.state('main.newrace', {
			url: 'newrace',
			views: {
				'content@': {
					templateUrl: 'js/race/page_race_new.html',
					controller: 'raceController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated
					}
				}
			}
		})
		.state('main.car', {
			url: 'car',
			views: {
				'content@': {
					templateUrl: 'js/car/page_car.html',
					controller: 'carController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
						master: function ($http, $auth) {
							return $http.get('getCarByUserId', {params: {userId: $auth.getPayload().sub}}).then(function (response) {
								return response.data;
							})
						},
						brands: function ($http) {
							return $http.get('getBrands').then(function (response) {
								return response.data;
							})
						}
					}
				}
			}
		})
		.state('main.club', {
			url: 'club',
			views: {
				'content@': {
					templateUrl: 'js/club/page_club.html',
					controller: 'clubController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
						clubs: function (Club) {
							return Club.query();
						}
					}
				}
			}
		})
		.state('main.racedetails', {
			url: 'racedetails/:race_id',
			views : {
				'content@': {
					templateUrl: 'js/race/page_race_details.html',
					controller: 'racedetailsController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
						race: function (Race, $stateParams) {
							return Race.get({id: $stateParams.race_id});
						},
						currentDriver: function ($http, $rootScope, $stateParams) {
							return $http.get('isUserInRace/' + $stateParams.race_id + '/' + $rootScope.user.id).then(function (response) {
								return response.data;
							})
						},
						cars: function ($http, $rootScope) {
							return $http.get('getCarByUserId', {params: {userId: $rootScope.user.id}}).then(function (response) {
								return response.data;
							})
						},
						drivers: function($http, $stateParams) {
							return $http.get('getDriversByRace', {params: {raceId: $stateParams.race_id}}).then(function (response) {
								return response.data;
							})
						}
					}
				}
			}
		})
		.state('main.adminclub', {
			url: 'adminclub',
			views: {
				'content@': {
					templateUrl: 'js/club/page_club_new.html',
					controller: 'adminclubController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated
					}
				}
			}
		})
		.state('main.clubdetails', {
			url: 'clubdetails/:club_id',
			views: {
				'content@': {
					templateUrl: 'js/club/page_club_details.html',
					controller: 'clubdetailsController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
						club: function (Club, $stateParams) {
							return Club.get({id: $stateParams.club_id});
						},
						races: function ($http, $stateParams) {
							return $http.get('getRacesByClub', {params: {clubId: $stateParams.club_id}}).then(function (response) {
								return response.data;
							});
						},
						userInClub: function ($http, $stateParams, $rootScope) {
							return $http.get('isUserInClub', {
								params: {
									userId: $rootScope.user.id,
									clubId: $stateParams.club_id
								}
							}).then(function (response) {
								return response.data;
							});
						},
						trainings: function ($http, $stateParams) {
							return $http.get('getTrainingsByClub', {params: {clubId: $stateParams.club_id}}).then(function (response) {
								return response.data;
							});
						}
					}
				}
			}
		})
		.state('main.mgtclub', {
			url: 'mgtclub',
			views: {
				'content@': {
					templateUrl: 'js/club/page_club_owner.html',
					controller: 'clubmgtController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
						currentClub: function (Club, $rootScope) {
							return Club.get({id: $rootScope.ownerClub.id});
						},
						races: function ($http, $rootScope) {
							return $http.get('getRacesByClub', {params: {clubId: $rootScope.ownerClub.id}}).then(function (response) {
								return response.data;
							});
						}
					}
				}
			}
		})
		.state('main.profile', {
			url: 'profile',
			views: {
				'content@': {
					templateUrl: 'js/user/profile.html',
					controller: 'profileController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
						profile: function ($http, $auth, $rootScope) {
							return $http.get('user/' + $auth.getPayload().sub).then(function (response) {
								$rootScope.authenticated = true;
								$rootScope.isOwner = response.data.owner;
								$rootScope.haveClub = response.data.userHasClub;
								$rootScope.isPremium = response.data.premium;
								$rootScope.user = response.data;
								if ($rootScope.isOwner) {
									$http.get('getOwnerClub', {params: {userId :$rootScope.user.id }}).success(function(data){
										$rootScope.ownerClub = data;
									});
								}
								return response.data;
							});
						}
					}
				}
			}
		})
		.state('main.closerace', {
			url: 'closerace/:race_id',
			views: {
				'content@': {
					templateUrl: 'js/race/page_race_close.html',
					controller: 'closeRaceController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
						race: function (Race, $stateParams) {
							return Race.get({id: $stateParams.race_id});
						}
					}
				}
			}
		})
		.state('main.yourclub', {
			url: 'yourclub',
			views: {
				'content@': {
					templateUrl: 'js/club/page_club.html',
					controller: 'yourclubController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
						clubs: function ($http, $rootScope) {
							return $http.get('getClubsByUserId/' + $rootScope.user.id).then(function (response) {
								return response.data;
							})
						}
					}
				}
			}
		})
		.state('main.carDetails', {
			url: 'cardetails/:car_id',
			views: {
				'content@': {
					templateUrl: 'js/car/page_car_details.html',
					controller: 'cardetailsController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
						car: function (Car, $stateParams) {
							return Car.get({id: $stateParams.car_id});
						},
						motors: function($http) {
							return $http.get('getMotors').then(function (response) {
								return response.data;
							});
						},
						escList: function($http) {
							return $http.get('getEsc').then(function (response) {
								return response.data;
							});
						},
						settings: function($http, $stateParams) {
							return $http.get('getSettingsByCarId/' + $stateParams.car_id).then(function (response) {
								return response.data;
							});
						}
					}
				}
			}
		})
		.state('main.clubtraining', {
			url: 'settraining/:club_id',
			views: {
				'content@': {
					templateUrl: 'js/club/page_club_training.html',
					controller: 'clubTrainingController',
					resolve: {
						redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
						currentClub: function (Club, $stateParams) {
							return Club.get({id: $stateParams.club_id});
						}
					}
				}
			}
		}).state('main.yourraces', {
		url: 'yourraces',
		views: {
			'content@': {
				templateUrl: 'js/race/page_race_user.html',
				controller: 'yourRacesController',
				resolve: {
					redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
					joinedRaces: function($http, $rootScope) {
						return $http.get('getRacesByUserId', {params: {userId: $rootScope.user.id}}).then(function (response) {
							return response.data;
						});
					}
				}
			}
		}
	}).state('main.payment', {
		url: 'paymentdone',
		views: {
			'content@': {
				templateUrl: 'js/payment/page_payment_done.html',
				controller: 'paymentController',
				resolve: {
					redirectIfNotAuthenticated: _redirectIfNotAuthenticated,
					profile: function ($http, $auth, $rootScope) {
						return $http.get('user/' + $auth.getPayload().sub).then(function (response) {
							$rootScope.authenticated = true;
							$rootScope.isOwner = response.data.owner;
							$rootScope.haveClub = response.data.userHasClub;
							$rootScope.isPremium = response.data.premium;
							$rootScope.user = response.data;
							if ($rootScope.isOwner) {
								$http.get('getOwnerClub', {params: {userId :$rootScope.user.id }}).success(function(data){
									$rootScope.ownerClub = data;
								});
							}
							return response.data;
						});
					}
				}
			}
		}
	})
		.state('main.signup', {
			url: 'signup',
			views: {
				'content@': {
					templateUrl: 'signup.html',
					controller: 'signupController'
				}
			}
		});

	function _redirectIfNotAuthenticated($q, $state, $auth) {
		var defer = $q.defer();
		if($auth.isAuthenticated()) {
			defer.resolve();
		} else {
			$timeout(function () {
				console.log('not authenticated!');
				$state.go('main');
			});
			defer.reject();
		}
		return defer.promise;
	}

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	$httpProvider.interceptors.push('loginInterceptor');
});
