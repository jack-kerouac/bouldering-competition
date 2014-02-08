var gymFloorPlanModule = angular.module('gymFloorPlan', ['imageMap']);

var gymFloorPlanDirective = function () {

	return {
		restrict: 'E',
		template: '<image-map image="image" map-click="mapClickHandler(point)" marker-click="markerClickHandler(marker)" markers="markers"></image-map>',
		scope: {
			floorPlan: '=',
			floorPlanClickHandler: '&floorPlanClick',
			boulderClickHandler: '&boulderClick',
			boulders: '=',
			bouldersDraggable: '@'
		},
		controller: ['$scope', '$element', '$attrs', '$q', function ($scope, $element, $attrs, $q) {
			var icon = L.divIcon({
				className: 'boulder-marker',
//				html: '&#xf172;', // location marker
				html: '&#xf1a4;', // record
//				html: '&#xf1f6;', // target
				iconSize: [20, 23],
				iconAnchor: [9, 12]
			});

			function boulderForMarker(marker) {
				return _.find($scope.boulders, function (boulder) {
					return boulder.id === marker.id;
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
							leafletIcon: icon,
							x: boulder.location.x * $scope.floorPlan.img.widthInPx,
							y: boulder.location.y * $scope.floorPlan.img.heightInPx,
							color: boulder.color,
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