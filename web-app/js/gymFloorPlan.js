var gymFloorPlanModule = angular.module('gymFloorPlan', ['imageMap']);

var gymFloorPlanDirective = function () {

	function colorDiv(color, div) {
		var css;

		if (color.hasOwnProperty('ternary')) {
			// use text gradient for two colored boulders
			var gradient = color.primary + " 33%, " +
				color.secondary + " 33%, " + color.secondary + " 67%, " +
				color.ternary + " 67%";

			css = {
				background: "-webkit-linear-gradient(top, " + gradient + ")", /* Chrome10+,Safari5.1+ */
				background: "-o-linear-gradient(top, " + gradient + ")", /* Opera 11.10+ */
				background: "-ms-linear-gradient(top, " + gradient + ")", /* IE10+ */
				background: "linear-gradient(to bottom, " + gradient + ")" /* W3C */
			};
		}
		else if (color.hasOwnProperty('secondary')) {
			// use text gradient for two colored boulders
			var gradient = color.primary + " 50%, " + color.secondary + " 50%";

			css = {
				background: "-webkit-linear-gradient(top, " + gradient + ")", /* Chrome10+,Safari5.1+ */
				background: "-o-linear-gradient(top, " + gradient + ")", /* Opera 11.10+ */
				background: "-ms-linear-gradient(top, " + gradient + ")", /* IE10+ */
				background: "linear-gradient(to bottom, " + gradient + ")" /* W3C */
			};
		}
		else {
			css = {
				background: color.primary
			};
		}
		$(div).css(css);
	}

	L.BoulderIcon = L.DivIcon.extend({
		options: {
			className: 'boulder-icon',
			// html: '&#xf172;', // location marker
			// html: '&#xf1a4;', // record
			// html: '&#xf1f6;', // target
			iconSize: undefined,    // set with CSS (assignment to undefined is required!)
			iconAnchor: undefined,   // set with CSS (assignment to undefined is required!)
			color: { primary: 'black' }
		},
		createIcon: function (oldIcon) {
			var div = L.DivIcon.prototype.createIcon.call(this, oldIcon);
			var color = this.options.color;

			colorDiv(color, div);
			return div;
		}
	});

	L.boulderIcon = function (color) {
		return new L.BoulderIcon({color: color});
	}


	return {
		restrict: 'E',
		template: '<image-map image="image" map-click="mapClickHandler(point)" marker-click="markerClickHandler(marker)" markers="markers"></image-map>',
		scope: {
			floorPlan: '=',
			floorPlanClickHandler: '&floorPlanClick',
			boulderClickHandler: '&boulderClick',
			boulders: '=',
			bouldersDraggable: '@',
			selected: '='
		},
		controller: ['$scope', '$element', '$attrs', '$q', function ($scope, $element, $attrs, $q) {

			function boulderForMarker(marker) {
				return _.find($scope.boulders, function (boulder) {
					return boulder.id === marker.id;
				});
			}

			function markerForBoulder(boulder) {
				return _.find($scope.markers, function (marker) {
					return marker.id === boulder.id;
				});
			}

			var floorPlanReady = $q.defer();
			$scope.$watch('floorPlan', function (floorPlan, oldFloorPlan) {
				if (floorPlan === undefined)
					return;

				floorPlanReady.resolve(floorPlan);

				$scope.image = floorPlan.img;
			});

			$scope.$watch('boulders', function (boulders) {
				floorPlanReady.promise.then(function () {
					$scope.markers = _.map(boulders, function (boulder) {
						if (!boulder.location.floorPlan.id === $scope.floorPlan.id)
							throw new Error('boulder ' + boulder.id + ' is not on floor plan ' + $scope.floorPlan.id);

						return {
							id: boulder.id,
							leafletIcon: L.boulderIcon(boulder.color),
							x: boulder.location.x * $scope.floorPlan.img.widthInPx,
							y: boulder.location.y * $scope.floorPlan.img.heightInPx,
							draggable: $scope.bouldersDraggable
						}
					});
				});
			}, true);

			$scope.$watch('markers', function (markers) {
				_.each(markers, function (marker) {
					var boulder = boulderForMarker(marker);
					boulder.location.x = marker.x / $scope.floorPlan.img.widthInPx;
					boulder.location.y = marker.y / $scope.floorPlan.img.heightInPx;
				});
			}, true);

			$scope.$watch('selected', function (selectedBoulder, previouslySelectedBoulder) {
				if (previouslySelectedBoulder !== undefined)
					markerForBoulder(previouslySelectedBoulder).selected = false;

				if (selectedBoulder !== undefined)
					markerForBoulder(selectedBoulder).selected = true;
			});

			$scope.mapClickHandler = function (point) {
				$scope.floorPlanClickHandler({point: point});
			}

			$scope.markerClickHandler = function (marker) {
				var boulder = boulderForMarker(marker);
				$scope.boulderClickHandler({boulder: boulder});
			}
		}]
	};
};
gymFloorPlanModule.directive('gymFloorPlan', gymFloorPlanDirective);