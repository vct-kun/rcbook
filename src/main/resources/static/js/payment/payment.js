angular.module('payment', []).controller('paymentController', function($scope, profile, $state, $timeout) {
    $timeout(function(){
       $state.go('main.yourraces');
    }, 3000);
});