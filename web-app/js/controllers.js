var chalkUpControllers = angular.module('chalkUpControllers', ['chalkUpServices']);

var sessionCtrl = function ($scope, $http, gym, boulder, floorPlan) {
	$scope.date = moment().format('YYYY-MM-DD');

	$scope.gyms = gym.query();

	$scope.boulderOrderProp = 'id';
	$scope.$watch('gym', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;
		var gym = newValue;

		var fp = floorPlan.init(gym.floorPlans[0], $('div.map'));

		$scope.ascents = {};

		function setAscentStyle(boulder, style) {
			if(style === "none") {
				delete $scope.ascents[boulder.id];
			}
			else {
				$scope.ascents[boulder.id] = style;
			}
			$scope.$apply();
		}

		function getAscentStyle(boulder) {
			return $scope.ascents[boulder.id];
		}

		$scope.boulders = boulder.query({gymId: gym.id}, function (boulders) {
			var markers = [];
			$.each(boulders, function (index, boulder) {
				var marker = fp.mark(boulder);
				fp.markerAscentPopup(marker, boulder, getAscentStyle, setAscentStyle);
				markers.push(marker);
			});


			var layerGroups = fp.layerControlByColor(markers);
			// show all layers
			for (var layerGroup in layerGroups) {
				layerGroups[layerGroup].addTo(fp.map);
			}
		});
	});


	$scope.logSession = function() {
		console.log($scope);
	}

};
sessionCtrl.$inject = ['$scope', '$http', 'gym', 'boulder', 'floorPlan'];
chalkUpControllers.controller('SessionCtrl', sessionCtrl);


var boulderCtrl = function ($scope) {
	window.initChalkUp()
}
boulderCtrl.$inject = ['$scope'];
chalkUpControllers.controller('BoulderCtrl', boulderCtrl);


var statisticsCtrl = function ($scope) {
	window.initChalkUp()
}
statisticsCtrl.$inject = ['$scope'];
chalkUpControllers.controller('StatisticsCtrl', statisticsCtrl);


