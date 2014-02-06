var chalkUpControllers = angular.module('chalkUpControllers', ['chalkUpServices', 'imageMap']);

var sessionCtrl = function ($scope, $http, $window, Gym, FloorPlan, User, BoulderingSession) {

	$scope.session = {};
	$scope.session.date = moment().format('YYYY-MM-DD');
	// TODO: get it differently, somehow
	$scope.session.boulderer = User.get({userId: parseInt($('input[name="boulderer.id"]').val())});

	$scope.gyms = Gym.query();

	$scope.$watch('gyms', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;

		$scope.session.gym = $scope.gyms[0];
	}, true);

	$scope.boulderOrderProp = 'id';
	$scope.$watch('session.gym', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;
		var gym = newValue;

		// reset ascents
		// we cannot use an object which would be much easier, but Grails databinding requires a stupid set
		$scope.session.ascents = [];


		if ($scope.floorPlan)
			$scope.floorPlan.destroy();
		if (gym.floorPlans.length > 1)
			throw new Error("gym has more than one floor plan, make it selectable");
		$scope.floorPlan = FloorPlan.init(gym.floorPlans[0], $('div.map'));

		$scope.boulders = Gym.boulders({gymId: gym.id}, function (boulders) {
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


			$scope.markers = [];
			$.each(boulders, function (index, boulder) {
				var marker = $scope.floorPlan.mark(boulder);
				$scope.floorPlan.markerAscentPopup(marker, boulder, getAscentStyle, setAscentStyle);
				$scope.markers.push(marker);
			});


			$scope.layerGroups = $scope.floorPlan.groupByColor($scope.markers);
			// show all layers
			for (var layerGroup in $scope.layerGroups) {
				$scope.layerGroups[layerGroup].addTo($scope.floorPlan.map);
			}
			// add control
			$scope.layerControl = L.control.layers({}, $scope.layerGroups);
			$scope.layerControl.addTo($scope.floorPlan.map);
		});
	});

	$scope.logSession = function () {
		BoulderingSession.save($scope.session, function () {
				$window.location.href = '/statistics';
			},
			function (httpResponse) {
				$scope.errors = httpResponse.data.errors;
			});
	}

};
sessionCtrl.$inject = ['$scope', '$http', '$window', 'Gym', 'FloorPlan', 'User', 'BoulderingSession'];
chalkUpControllers.controller('SessionCtrl', sessionCtrl);


var boulderCtrl = function ($scope, $window, Gym, Grades, FloorPlan, Boulder) {

	$scope.boulders = {};

	function findColor(name) {
		return _.find($scope.boulders.gym.colors, function (color) {
			return color.name == name;
		});
	}

	$scope.gyms = Gym.query();
	$scope.$watch('gyms', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;

		$scope.boulders.gym = $scope.gyms[0];
	}, true);


	$scope.$watch('boulders.gym', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;

		var gym = newValue;

		if (gym.floorPlans.length > 1)
			throw new Error("gym has more than one floor plan, make it selectable");
		$scope.boulders.floorPlan = gym.floorPlans[0];

		$scope.boulders.color = gym.colors[0].name;
		$scope.boulders.coordinates = [];

		if ($scope.floorPlan)
			$scope.floorPlan.destroy();

		$scope.floorPlan = FloorPlan.init(gym.floorPlans[0], $('div.map'));
		$scope.floorPlan.cursor('crossHair');

		$scope.markers = [];

		$scope.floorPlan.map.on('click', function (e) {
			var markerNumber = $scope.markers.length;

			var marker = $scope.floorPlan.addMarker(e, {draggable: true});
			var color = findColor($scope.boulders.color);
			marker.setColor(color);

			$scope.markers.push(marker);
			var p = marker.getPoint();
			$scope.boulders.coordinates.push({
				x: p.x / $scope.floorPlan.width,
				y: p.y / $scope.floorPlan.height
			});

			marker.on('dragend', function (e) {
				var marker = e.target;
				var p = marker.getPoint();
				$scope.boulders.coordinates[markerNumber] = {
					x: p.x / $scope.floorPlan.width,
					y: p.y / $scope.floorPlan.height
				};
			});
		});
	});


	$scope.$watch('boulders.color', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;
		var color = findColor(newValue);
		_.map($scope.markers, function (marker) {
			marker.setColor(color);
		});
	});


	$scope.boulders.gradeCertainty = 'UNKNOWN';

	$scope.grades = Grades.query();

	$scope.registerBoulders = function () {
		if ($scope.boulders.coordinates.length === 0) {
			// TODO: proper error handling
			$scope.errors = { message: "must at least set one boulder" };
			return;
		}

		Boulder.save($scope.boulders, function () {
				$window.location.href = '/home';
			},
			function (httpResponse) {
				$scope.errors = httpResponse.data.errors;
			});
	}
}
boulderCtrl.$inject = ['$scope', '$window', 'Gym', 'Grades', 'FloorPlan', 'Boulder'];
chalkUpControllers.controller('BoulderCtrl', boulderCtrl);


var statisticsCtrl = function ($scope, Grades, User) {
	$scope.chart = {};

	var gradeColor = 'rgb(203,75,75)';
	var ascentCountColor = 'rgb(175,216,248)';

	$scope.chart.options = {
		xaxis: {
			mode: "time"
//			tickSize: [1, "day"]
		},
		yaxes: [
			{
				min: 0.3,
				max: 0.9,
				font: { color: gradeColor }
			},
			{
				tickDecimals: 0,
				position: 'right',
				font: { color: ascentCountColor }
			}
		],
		series: {
			lines: { show: true, fill: false },
			points: { show: true, fill: false }
		}
	};

	Grades.query(function (grades) {
		var gradeTicks = _.map(grades, function (grade) {
			return [grade.value, grade.font];
		});
		$scope.chart.options.yaxes[0].ticks = gradeTicks;
	});

	$scope.chart.data = [];

	// TODO: get user ID differently, somehow
	$scope.user = User.get({ userId: $('input[name="boulderer.id"]').val()});

	$scope.$watch('user', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;
		var user = newValue;

		$scope.statistics = User.statistics({userId: user.id});
	}, true);

	$scope.$watch('statistics', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;
		var statistics = newValue;

		var currentGradeData = [];
		currentGradeData.push([moment($scope.user.registrationDate).toDate(), $scope.user.initialGrade.value]);

		var ascentCountData = [];

		_.each(statistics, function (stat) {
			var date = moment(stat.session.date).toDate();
			var grade = stat.grade.mean.value;
			currentGradeData.push([date, grade]);
			var ascentCount = stat.session.ascents.length;
			ascentCountData.push([date, ascentCount]);
		});

		// TODO: get labels differently
		$scope.chart.data = [
			{ data: currentGradeData, label: $('#sessions thead th.grade').text(), color: gradeColor, yaxis: 1 },
			{ data: ascentCountData, label: $('#sessions thead th.ascent-count').text(), color: ascentCountColor, yaxis: 2 }
		];

	}, true)
};
statisticsCtrl.$inject = ['$scope', 'Grades', 'User'];
chalkUpControllers.controller('StatisticsCtrl', statisticsCtrl);


var startCtrl = function ($scope, Gym) {

	$scope.gyms = Gym.query();

}
startCtrl.$inject = ['$scope', 'Gym'];
chalkUpControllers.controller('StartCtrl', startCtrl);


var gymOverviewCtrl = function ($scope) {
	$scope.fp = {
		"id": 1,
		"img": {
			"widthInPx": 2000,
			"heightInPx": 1393,
			"url": "http://localhost:8080/images/floorPlans/boulderwelt-muenchen.jpg"
		}
	};

	$scope.c = function (point) {
		$scope.ms.push({
			id: 3,
			leafletIcon: icon,
			x: point.x,
			y: point.y,
			draggable: true
		});
		console.log(point);
	}

	$scope.cl = function (marker) {
		console.log(marker);
	}

	$scope.bs = [
		{
			"id": 1,
			"color": {
				"name": "RED",
				"germanName": "rot",
				"englishName": "red",
				"primary": "rgb(217, 0, 0)"
			},
			"grade": {
				"mean": {
					"value": 0.4242424242,
					"font": "5C"
				},
				"variance": 0.07134709913526724,
				"sigma": 0.2671087777203648
			},
			"description": null,
			"end": null,
			"initialGrade": {
				"certainty": "RANGE",
				"readable": "1A – 8A",
				"gradeLow": {
					"value": 0.01515151515,
					"font": "1A"
				},
				"gradeHigh": {
					"value": 0.83333333325,
					"font": "8A"
				}
			},
			"location": {
				"floorPlan": {
					"id": 1,
					"img": {
						"widthInPx": 2000,
						"heightInPx": 1393,
						"url": "http://localhost:8080/images/floorPlans/boulderwelt-muenchen.jpg"
					}
				},
				"x": 0.267,
				"y": 0.2139267767
			},
			"gym": 1
		},
		{
			"id": 2,
			"color": {
				"name": "RED",
				"germanName": "rot",
				"englishName": "red",
				"primary": "rgb(217, 0, 0)"
			},
			"grade": {
				"mean": {
					"value": 0.4242424242,
					"font": "5C"
				},
				"variance": 0.07134709913526724,
				"sigma": 0.2671087777203648
			},
			"description": null,
			"end": null,
			"initialGrade": {
				"certainty": "RANGE",
				"readable": "1A – 8A",
				"gradeLow": {
					"value": 0.01515151515,
					"font": "1A"
				},
				"gradeHigh": {
					"value": 0.83333333325,
					"font": "8A"
				}
			},
			"location": {
				"floorPlan": {
					"id": 1,
					"img": {
						"widthInPx": 2000,
						"heightInPx": 1393,
						"url": "http://localhost:8080/images/floorPlans/boulderwelt-muenchen.jpg"
					}
				},
				"x": 0.3715,
				"y": 0.2462311558
			},
			"gym": 1
		}
	];

	var icon = L.divIcon({
		className: 'boulder-marker',
		html: '&#xf172;',
		iconSize: undefined // set in CSS
	});

	$scope.ms = _.map($scope.bs, function (boulder) {
			return {
				id: boulder.id,
				leafletIcon: icon,
				x: boulder.location.x * boulder.location.floorPlan.img.widthInPx,
				y: boulder.location.y * boulder.location.floorPlan.img.heightInPx,
				color: boulder.color
			}
		});
};
gymOverviewCtrl.$inject = ['$scope'];
chalkUpControllers.controller('GymOverviewCtrl', gymOverviewCtrl);