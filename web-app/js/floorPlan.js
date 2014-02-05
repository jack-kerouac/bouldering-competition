var floorPlanModule = angular.module('floorPlan', ['leaflet-directive']);

var floorPlanDirective = function () {
	return {
		restrict: 'E',
		template: '<leaflet center="center" defaults="defaults" height="600" markers="markers"></leaflet>',
		priority: 20,
		scope: {
			floorPlan: '=plan',
			click: '&',
			boulders: '='
		},
		controller: ['$scope', '$element', '$attrs', '$q', 'leafletData', function ($scope, $element, $attrs, $q, leafletData) {
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

			var calcDefer = $q.defer();
			$scope.calc = calcDefer.promise;

			leafletData.getMap().then(function (map) {
				L.control.fullscreen({
					position: 'topleft',
					forceSeparateButton: true
				}).addTo(map);

				var containerWidth = map.getSize().x;
				var containerHeight = map.getSize().y;
				var containerAspectRatio = containerHeight / containerWidth;

				var floorPlanWidth = $scope.floorPlan.img.widthInPx;
				var floorPlanHeight = $scope.floorPlan.img.heightInPx;
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

				L.imageOverlay($scope.floorPlan.img.url, bounds, {
						noWrap: true
					}
				).addTo(map);
				map.setMaxBounds(bounds);


				var scaleFactor = floorPlanWidth / resultingWidth;
				calcDefer.resolve({
					latLngToFloorPlanAbs: function (latLng) {
						return map.project(L.latLng(latLng), 0).add([resultingWidth / 2, resultingHeight / 2]).multiplyBy(scaleFactor);
					},
					floorPlanAbsToLatLng: function (point) {
						return map.unproject(L.point(point).divideBy(scaleFactor).subtract([resultingWidth / 2, resultingHeight / 2]));
					}
				});
			});

			$scope.$on('leafletDirectiveMap.click', function (event, attr) {
				var point = $scope.latLngToFloorPlanAbs(attr.leafletEvent.latlng);
				$scope.click({point: point});
			});

			this.getScope = function() {
				return $scope;
			};
		}]
	};
};
floorPlanModule.directive('floorPlan', floorPlanDirective);


var boulders = function () {
	return {
		restrict: 'A',
		require: 'floorPlan',
		priority: 10,
		scope: false,
		link: function($scope, element, attrs, floorPlanCtrl) {
			var floorPlanScope = floorPlanCtrl.getScope();

			// TODO: move this out of this watch (make it similar to leaflet.getMap(), using promises...?)
			$scope.$watch('boulders', function (boulders) {
				if (boulders === undefined)
					return;

				floorPlanScope.calc.then(function(calc) {
					var markersArray = _.map(boulders, function (boulder) {
						var marker = {};
						var latlng = calc.floorPlanAbsToLatLng([boulder.location.x * $scope.floorPlan.img.widthInPx, boulder.location.y * $scope.floorPlan.img.heightInPx]);
						marker.name = boulder.id;
						marker.lat = latlng.lat;
						marker.lng = latlng.lng;
						return marker;
					});
					floorPlanScope.markers = _.indexBy(markersArray, 'name');
				});
			}, true);

			$scope.where = "boulders attribute";

		}
	};
};
floorPlanModule.directive('boulders', boulders);