var chalkUpControllers = angular.module('chalkUpControllers', ['chalkUpServices', 'gymFloorPlan', 'ngRoute']);

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

	$scope.$watch('session.gym', function (newValue, oldValue) {
		if (newValue === oldValue)
			return;
		var gym = newValue;

		// reset ascents
		$scope.ascents = {};

		$scope.boulders = Gym.boulders({gymId: gym.id});
	});

	$scope.select = function (boulder) {
		$scope.currentBoulder = boulder;
	};

	$scope.removeAscentIfStyleNone = function(boulderId) {
		if($scope.ascents[boulderId] === 'none')
			delete $scope.ascents[boulderId];
	}


	$scope.boulder = function(id) {
		return _.find($scope.boulders, function(boulder) {
			return boulder.id == id;
		});
	}


	$scope.logSession = function () {
		// transform object since Grails databinding requires a stupid set
		$scope.session.ascents = _.map($scope.ascents, function (style, boulderId) {
			return {boulder: { id: boulderId }, style: style};
		})

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


var gymOverviewCtrl = function ($scope, $location, $routeParams, Gym, Boulder, User) {

	User.query(function (users) {
		$scope.users = _.indexBy(users, 'id');
	});


	var gymId = ($location.search()).gymId;
	$scope.gym = Gym.get({gymId: gymId});
	$scope.boulders = Gym.boulders({gymId: gymId});
	$scope.sessions = Gym.sessions({gymId: gymId});


	$scope.currentBoulder = undefined;

	$scope.$watch('currentBoulder', function (boulder) {
		if (boulder === undefined)
			return;

		$scope.currentBoulderAscents = Boulder.ascents({boulderId: boulder.id});
	})

	$scope.select = function (boulder) {
		$scope.currentBoulder = boulder;
	};

};
gymOverviewCtrl.$inject = ['$scope', '$location', '$routeParams', 'Gym', 'Boulder', 'User'];
chalkUpControllers.controller('GymOverviewCtrl', gymOverviewCtrl);