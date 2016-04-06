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
});