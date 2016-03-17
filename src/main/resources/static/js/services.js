/**
 * Created by vctran on 17/03/16.
 */
angular.module('rcbook.services', []).factory('Race', function($location, $resource) {
    var host = $location.protocol() + "://" + $location.host() + ":" + $location.port() + "/" + $location.absUrl().split("/")[3];
    return $resource(host + '/race/:id');
});