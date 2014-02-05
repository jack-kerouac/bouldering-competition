var floorPlanModule = angular.module('floorPlan', ['leaflet-directive']);

var floorPlanDirective = function () {
	return {
		restrict: 'E',
		template: '<leaflet center="center" defaults="defaults" height="600" markers="markers"></leaflet>',
		scope: {
			image: '=',
			click: '&',
			boulders: '='
		},
		controller: ['$scope', '$element', '$attrs', 'leafletData', function ($scope, $element, $attrs, leafletData) {
			$scope.defaults = {
				crs: 'Simple',
				maxZoom: 3
			};
			$scope.center = {
				lat: 0,
				lng: 0,
				zoom: 0
			};
			// this is required for the leaflet directive to display markers at all
			$scope.markers = {};

			leafletData.getMap().then(function (map) {
				L.control.fullscreen({
					position: 'topleft',
					forceSeparateButton: true
				}).addTo(map);

				var containerWidth = map.getSize().x;
				var containerHeight = map.getSize().y;
				var containerAspectRatio = containerHeight / containerWidth;

				var floorPlanWidth = $scope.image.widthInPx;
				var floorPlanHeight = $scope.image.heightInPx;
				var floorPlanAspectRatio = floorPlanHeight / floorPlanWidth;

				if (containerAspectRatio >= floorPlanAspectRatio) {
					// floor plan is wider, => fit to containerWidth
					var resultingWidth = containerWidth;
					var resultingHeight = floorPlanHeight * containerWidth / floorPlanWidth;
					var xOffset = 0;
					var yOffset = (containerHeight - resultingHeight) / 2;
				}
				else {
					// floor plan is higher, => fit to containerHeight
					var resultingWidth = floorPlanWidth * containerHeight / floorPlanHeight;
					var resultingHeight = containerHeight;
					var xOffset = (containerWidth - resultingWidth) / 2;
					var yOffset = 0;
				}

				var southWestCorner = map.containerPointToLatLng([xOffset, resultingHeight + yOffset]);
				var northEastCorner = map.containerPointToLatLng([xOffset + resultingWidth, yOffset]);


				var bounds = L.latLngBounds(southWestCorner, northEastCorner);

				L.imageOverlay($scope.image.url, bounds, {
						noWrap: true
					}
				).addTo(map);
				map.setMaxBounds(bounds);


				var scaleFactor = floorPlanWidth / resultingWidth;
				$scope.latLngToFloorPlanAbs = function (latLng) {
					return map.project(L.latLng(latLng), 0).add([resultingWidth / 2, resultingHeight / 2]).multiplyBy(scaleFactor);
				};

				$scope.floorPlanAbsToLatLng = function (point) {
					return map.unproject(L.point(point).divideBy(scaleFactor).subtract([resultingWidth / 2, resultingHeight / 2]));
				};
			});


			$scope.$on('leafletDirectiveMap.click', function (event, attr) {
				var point = $scope.latLngToFloorPlanAbs(attr.leafletEvent.latlng);
				$scope.click({point: point});
			});
		}]
	};
};
floorPlanModule.directive('floorPlan', floorPlanDirective);

