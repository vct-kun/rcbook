/**
 * Created by vctran on 17/03/16.
 */
angular.module('rcbook.services', []).factory('Race', function($resource) {
    return $resource('race/:id', { id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Club', function($resource) {
    return $resource('club/:id', { id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Car', function($resource) {
    return $resource('car/:id', { id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Driver', function($resource) {
    return $resource('driver/:id', { id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Setting', function($resource) {
    return $resource('setting/:id', { id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Training', function($resource) {
    return $resource('training/:id', { id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('loginInterceptor', function() {
    return {
        'request': function(config) {
            if (config.url.indexOf("auth") > -1) {
                config.url = config.url.substring(1);
            }
            return config;
        }
    }
});