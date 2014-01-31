var chalkUpControllers = angular.module('chalkUpControllers', ['chalkUpServices']);

var sessionCtrl = function ($scope, $http, $window, Gym, FloorPlan, User, BoulderingSession) {

	$scope.session = {};
	$scope.session.date = moment().format('YYYY-MM-DD');
	// TODO: get it differently, somehow
	$scope.user = User.get({userId: parseInt($('input[name="boulderer.id"]').val())}, function (user) {
		$scope.session.boulderer = user;
	});

	$scope.gyms = Gym.query(function (gyms) {
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

		if (gym.floorPlans.length > 1)
			throw new Error("gym has more than one floor plan, make it selectable");
		var fp = FloorPlan.init(gym.floorPlans[0], $('div.map'));

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

		$scope.boulders = Gym.boulders({gymId: gym.id}, function (boulders) {
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
		BoulderingSession.save($scope.session, function () {
				$window.location.href = '/boulderer/' + $scope.user.username + '/statistics';
			},
			function (httpResponse) {
				$scope.errors = httpResponse.data.errors;
			});
	}

};
sessionCtrl.$inject = ['$scope', '$http', '$window', 'Gym', 'FloorPlan', 'User', 'BoulderingSession'];
chalkUpControllers.controller('SessionCtrl', sessionCtrl);


var boulderCtrl = function ($scope, $window, Gym, Grades, FloorPlan, Boulder) {

	function findColor(name) {
		return _.find($scope.boulders.gym.colors, function (color) {
			return color.name == name;
		});
	}

	$scope.gyms = Gym.query(function (gyms) {
		$scope.boulders.gym = gyms[0];
	});

	$scope.$watch('boulders.gym', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;

		var gym = newValue;

		if (gym.floorPlans.length > 1)
			throw new Error("gym has more than one floor plan, make it selectable");
		$scope.boulders.floorPlan = gym.floorPlans[0];

		$scope.boulders.color = gym.colors[0].name;
		$scope.boulders.coordinates = [];

		var fp = FloorPlan.init(gym.floorPlans[0], $('div.map'));
		fp.cursor('crossHair');

		$scope.markers = [];

		fp.map.on('click', function (e) {
			var markerNumber = $scope.markers.length;

			var marker = fp.addMarker(e, {draggable: true});
			var color = findColor($scope.boulders.color);
			marker.setColor(color);

			$scope.markers.push(marker);
			var p = marker.getPoint();
			$scope.boulders.coordinates.push({
				x: p.x / fp.width,
				y: p.y / fp.height
			});

			marker.on('dragend', function (e) {
				var marker = e.target;
				var p = marker.getPoint();
				$scope.boulders.coordinates[markerNumber] = {
					x: p.x / fp.width,
					y: p.y / fp.height
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


	$scope.boulders = {};
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
	$scope.user = User.get({ userId: $('input[name="boulderer.id"]').val()}, function (user) {
		var currentGradeData = [];
		currentGradeData.push([moment(user.registrationDate).toDate(), user.initialGrade.value]);

		var ascentCountData = [];

		$scope.statistics = User.statistics({userId: user.id}, function (statistics) {
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
		});
	});

};
statisticsCtrl.$inject = ['$scope', 'Grades', 'User'];
chalkUpControllers.controller('StatisticsCtrl', statisticsCtrl);