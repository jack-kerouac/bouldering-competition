var floorPlanModule = angular.module('floorPlan', ['leaflet-directive']);

var floorPlanDirective = function () {

	var icon = {
		type: 'div',
		iconSize: undefined,     // set in CSS
		//popupAnchor:  [0, 0],
		className: 'boulder-marker',
		html: '&#xf172;'
	};

	function createMarkerForBoulder(boulder, latlng) {
		var marker = {};
		marker.name = boulder.id;
		marker.icon = icon;
		marker.lat = latlng.lat;
		marker.lng = latlng.lng;
		return marker;
	}

	return {
		restrict: 'E',
		template: '<leaflet center="center" defaults="defaults" height="600" markers="markers"></leaflet>',
		priority: 20,
		scope: {
			floorPlan: '=plan',
			clickHandler: '&click',
			boulders: '='
		},
		controller: ['$scope', '$element', '$attrs', '$q', 'leafletData', function ($scope, $element, $attrs, $q, leafletData) {
			// +++++++++
			// SETUP MAP
			// +++++++++
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

			$scope.mapCalc = calcDefer.promise;

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


			// +++++++++++++
			// CLICK HANDLER
			// +++++++++++++
			$scope.$on('leafletDirectiveMap.click', function (event, attr) {
				$scope.mapCalc.then(function (mapCalc) {
					var point = mapCalc.latLngToFloorPlanAbs(attr.leafletEvent.latlng);
					$scope.clickHandler({point: point});
				});
			});


			// ++++++++
			// BOULDERS
			// ++++++++
			$scope.$watch('boulders', function (boulders) {
				if (boulders === undefined)
					return;

				$scope.mapCalc.then(function (mapCalc) {
					var markersArray = _.map(boulders, function (boulder) {
						var latlng = mapCalc.floorPlanAbsToLatLng([
							boulder.location.x * $scope.floorPlan.img.widthInPx,
							boulder.location.y * $scope.floorPlan.img.heightInPx]);
						return createMarkerForBoulder(boulder, latlng);
					});
					$scope.markers = _.indexBy(markersArray, 'name');
				});
			}, true);

		}]
	};
};
floorPlanModule.directive('floorPlan', floorPlanDirective);