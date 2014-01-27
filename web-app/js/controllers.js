var chalkUpControllers = angular.module('chalkUpControllers', ['chalkUpServices']);

var sessionCtrl = function ($scope, $http, $window, gym, boulder, floorPlan, boulderingSession) {

	$scope.session = {};
	$scope.session.date = moment().format('YYYY-MM-DD');
	$scope.session.boulderer = { id: $('input[name="boulderer.id"]').val()}

	$scope.gyms = gym.query(function (gyms) {
		$scope.session.gym = gyms[0];
	});


	$scope.boulderOrderProp = 'id';
	$scope.$watch('session.gym', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;
		var gym = newValue;

		// reset ascents
		// we cannot use an object which would be much easier, but Grails databinding requires a stupid set
		$scope.session.ascents = [];

		var fp = floorPlan.init(gym.floorPlans[0], $('div.map'));

		function setAscentStyle(boulder, style) {
			var ascent = _.find($scope.session.ascents, function (ascent) {
				return ascent.boulder === boulder
			});
			if (ascent && style === "none") {
				var index = $scope.session.ascents.indexOf(ascent);
				$scope.session.ascents.splice(index, 1);
			}
			else if (ascent) {
				ascent.style = style;
			}
			else {
				ascent = {boulder: boulder, style: style};
				$scope.session.ascents.push(ascent);
			}

			$scope.$apply();
		}

		function getAscentStyle(boulder) {
			var ascent = _.find($scope.session.ascents, function (ascent) {
				return ascent.boulder === boulder
			});
			if (ascent)
				return ascent.style;
			else
				return undefined;
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


	$scope.logSession = function () {
		boulderingSession.save($scope.session, function() {
			$window.location.href = '/boulderer/flo/statistics';
		},
		function(httpResponse) {
			$scope.errors = httpResponse.data.errors;
		});
	}

};
sessionCtrl.$inject = ['$scope', '$http', '$window', 'gym', 'boulder', 'floorPlan', 'boulderingSession'];
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


