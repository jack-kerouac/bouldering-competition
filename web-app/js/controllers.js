var chalkUpControllers = angular.module('chalkUpControllers', ['chalkUpServices']);

var sessionCtrl = function ($scope, $http, gym, boulder) {
	$scope.gyms = gym.query();

	$scope.boulderOrderProp = 'id';
	$scope.$watch('gym', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;

		$scope.boulders = boulder.query({gymId: newValue.id}, function(boulders) {
			// TODO: this is crap, of course
			setTimeout(window.initChalkUp, 300);
		});
	});

};
sessionCtrl.$inject = ['$scope', '$http', 'gym', 'boulder'];
chalkUpControllers.controller('SessionCtrl', sessionCtrl);




var boulderCtrl = function($scope) {
	window.initChalkUp()
}
boulderCtrl.$inject = ['$scope'];
chalkUpControllers.controller('BoulderCtrl', boulderCtrl);


var statisticsCtrl = function($scope) {
	window.initChalkUp()
}
statisticsCtrl.$inject = ['$scope'];
chalkUpControllers.controller('StatisticsCtrl', statisticsCtrl);


