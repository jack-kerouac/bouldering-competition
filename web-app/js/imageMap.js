var imageMapModule = angular.module('imageMap', ['leaflet-directive']);

var imageMapDirective = function () {

	function createMarker(imageMarker, pointToLatLng) {
		var marker = {};
		marker.name = imageMarker.name;
		marker.icon = imageMarker.icon;
		var latlng = pointToLatLng([imageMarker.x, imageMarker.y]);
		marker.lat = latlng.lat;
		marker.lng = latlng.lng;
		return marker;
	}

	return {
		restrict: 'E',
		template: '<leaflet center="center" defaults="defaults" height="{{height}}" markers="markers"></leaflet>',
		scope: {
			image: '=',
			mapClickHandler: '&mapClick',
			markerClickHandler: '&markerClick',
			imageMarkers: '=imageMarkers',
			height: '='
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

				var imageWidth = $scope.image.widthInPx;
				var imageHeight = $scope.image.heightInPx;
				var imageAspectRatio = imageHeight / imageWidth;

				if (containerAspectRatio >= imageAspectRatio) {
					// image plan is wider, => fit to containerWidth
					var resultingWidth = containerWidth;
					var resultingHeight = imageHeight * containerWidth / imageWidth;
					var xOffset = 0;
					var yOffset = (containerHeight - resultingHeight) / 2;
				}
				else {
					// image plan is higher, => fit to containerHeight
					var resultingWidth = imageWidth * containerHeight / imageHeight;
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


				var scaleFactor = imageWidth / resultingWidth;
				calcDefer.resolve({
					latLngToImagePoint: function (latLng) {
						return map.project(L.latLng(latLng), 0).add([resultingWidth / 2, resultingHeight / 2]).multiplyBy(scaleFactor);
					},
					imagePointToLatLng: function (point) {
						return map.unproject(L.point(point).divideBy(scaleFactor).subtract([resultingWidth / 2, resultingHeight / 2]));
					}
				});
			});


			// +++++++++++++
			// CLICK HANDLER
			// +++++++++++++
			$scope.$on('leafletDirectiveMap.click', function (event, attr) {
				$scope.mapCalc.then(function (mapCalc) {
					var point = mapCalc.latLngToImagePoint(attr.leafletEvent.latlng);
					$scope.mapClickHandler({point: point});
				});
			});


			// +++++++
			// MARKERS
			// +++++++
			$scope.$watch('imageMarkers', function (imageMarkers) {
				if (imageMarkers === undefined)
					return;

				$scope.mapCalc.then(function (mapCalc) {
					var markersArray = _.map(imageMarkers, function (imageMarker) {
						return createMarker(imageMarker, mapCalc.imagePointToLatLng);
					});
					$scope.markers = _.indexBy(markersArray, 'name');
				});
			}, true);

		}]
	};
};
imageMapModule.directive('imageMap', imageMapDirective);