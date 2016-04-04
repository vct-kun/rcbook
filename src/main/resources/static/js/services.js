/**
 * Created by vctran on 17/03/16.
 */
angular.module('rcbook.services', []).factory('Race', function($location, $resource) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    return $resource(host + '/race/:id', { id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Club', function($location, $resource) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    return $resource(host + '/club/:id', { id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Car', function($location, $resource) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    return $resource(host + '/car/:id', { id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Driver', function($location, $resource) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    return $resource(host + '/driver/:id', { id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).service('UserService', function(store){
    var service = this;
    var currentUser = null;

    service.setCurrentUser = function(user) {
        currentUser = user;
        store.set('user', user);
        return currentUser;
    };

    service.getCurrentUser = function() {
        if (!currentUser) {
            currentUser = store.get('user');
        }
        return currentUser;
    }
}).service('APIInterceptor', function($rootScope, UserService){
    var service = this;

    service.request = function(config) {
        var currentUser = UserService.getCurrentUser(),
            access_token = currentUser ? currentUser.access_token : null;

        if (access_token) {
            config.headers.authorization = access_token;
        }
        return config;
    };

    service.responseError = function(response) {
        if (response.status == 401) {
            $rootScope.$broadcast('unauthorized');
        }
        return response;
    };
});