/**
 * This directive allows displaying an image as a leaflet map, adding markers to it and installing click handlers
 * for the image and the markers.
 *
 * The image is added as an image overlay and scaled so that it fills the entire map container. Zooming out further is
 * not allowed, zooming in is allowed for three more levels.
 * The image to be displayed is specified with the "image" attribute. This is expected to be an angular expression that
 * points to an object with the following properties:
 * - url
 * - widthInPx
 * - heightInPx
 *
 * Markers are added with the "markers" attribute. This is expected to be an angular expression that points to either
 * an array of image markers or an object with keys that map to arrays of image markers. In the former case, all the
 * image markers in the array are added as markers. In the latter case, layer groups are added, with each group
 * having the name of the key and containing the list of markers of the value of the object. Also, in this case, a
 * layer control is added to the map.
 *
 * Image markers are objects with the following properties:
 * - id
 * - x, y: The coordinates of the marker, relative to the image. Should be greater than 0 and less than image dimensions
 * - color (optional): Object with properties primary (and maybe secondary), defining the color of the marker. The properties are CSS color strings.
 * - leafletIcon (optional): An instance of L.icon defining the icon of the marker
 * - draggable (optional): whether the marker can be dragged on the map. If so, its x/y coordinates are updated upon dragend.
 */
var imageMapModule = angular.module('imageMap', []);

var imageMapDirective = function () {

	function calculateProjection(map, image) {
		var containerWidth = map.getSize().x;
		var containerHeight = map.getSize().y;
		var containerAspectRatio = containerHeight / containerWidth;

		var imageWidth = image.widthInPx;
		var imageHeight = image.heightInPx;
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

		var scaleFactor = imageWidth / resultingWidth;

		return {
			latLngBounds: L.latLngBounds(southWestCorner, northEastCorner),
			latLngToImagePoint: function (latLng) {
				return map.project(L.latLng(latLng), 0).add([resultingWidth / 2, resultingHeight / 2]).multiplyBy(scaleFactor);
			},
			imagePointToLatLng: function (point) {
				return map.unproject(L.point(point).divideBy(scaleFactor).subtract([resultingWidth / 2, resultingHeight / 2]), 0);
			}
		}
	}

	function colorIcon(marker, color) {
		var $icon = $(marker._icon);

		if (color === undefined) {
			$icon.css({
				color: 'black',
				background: '',
				'-webkit-background-clip': '',
				'-webkit-text-fill-color': ''
			});
			return;
		}

		if (!color.hasOwnProperty('secondary')) {
			$icon.css({
				color: color.primary,
				background: '',
				'-webkit-background-clip': '',
				'-webkit-text-fill-color': ''
			});
		}
		else {
			// use text gradient for two colored boulders
			$icon.css({
				color: 'black',
				background: 'linear-gradient(' + color.primary + ', ' + color.secondary + ')',
				'-webkit-background-clip': 'text',
				'-webkit-text-fill-color': 'transparent'
			});
		}
	};


	function createMarker(imageMarker, $scope) {
		var latLng = $scope.projection.imagePointToLatLng([imageMarker.x, imageMarker.y]);
		var options = {
			id: imageMarker.id,
			riseOnHover: true,
			draggable: imageMarker.draggable
		};
		if (imageMarker.leafletIcon)
			options.icon = imageMarker.leafletIcon;

		var marker = L.marker(latLng, options);

		$scope.$watch(function () {
			return imageMarker.color;
		}, function (color) {
			colorIcon(marker, color);
		});

		marker.on('dragend', function (e) {
			$scope.$apply(function () {
				var marker = e.target;
				var p = $scope.projection.latLngToImagePoint(marker.getLatLng());
				imageMarker.x = p.x;
				imageMarker.y = p.y;
			});
		});

		return marker;
	}

	return {
		restrict: 'E',
		template: '<div class="map"></div>',
		scope: {
			image: '=',
			mapClickHandler: '&mapClick',
			markerClickHandler: '&markerClick',
			imageMarkers: '=markers'
		},
		controller: ['$scope', '$element', '$attrs', '$q', function ($scope, $element, $attrs, $q) {
			// +++++++++
			// SETUP MAP
			// +++++++++
			var leafletMapElem = $element[0].children[0];
			var map = L.map(leafletMapElem, {
				maxZoom: 3,
				crs: L.CRS.Simple
			});
			map.setView([0, 0], 0);

			L.control.fullscreen({
				position: 'topleft',
				forceSeparateButton: true
			}).addTo(map);


			$scope.projection = calculateProjection(map, $scope.image);

			L.imageOverlay($scope.image.url, $scope.projection.latLngBounds, {
					noWrap: true
				}
			).addTo(map);
			map.setMaxBounds($scope.projection.latLngBounds);


			// +++++++++++++
			// CLICK HANDLER
			// +++++++++++++
			map.on('click', function (event) {
				$scope.$apply(function () {
					var point = $scope.projection.latLngToImagePoint(event.latlng);
					$scope.mapClickHandler({point: point});
				});
			});

			// +++++++
			// MARKERS
			// +++++++
			$scope.$watch('imageMarkers', function (imageMarkers, oldImageMarkers) {
				if (imageMarkers === undefined)
					return;

				// DELETE OLD MARKERS

				if (imageMarkers !== oldImageMarkers) {
					if (!$scope.hasLayers) {
						_.each($scope.leafletMarkers, function (leafletMarker) {
							map.removeLayer(leafletMarker);
						});
						delete $scope.leafletMarkers;
					}
					else {
						_.each($scope.leafletMarkerLayers, function (layerGroup, layerName) {
							map.removeLayer(layerGroup);
						});
						delete $scope.leafletMarkerLayers;

						map.removeControl($scope.layerControl);
						delete $scope.layerControl;
					}
				}


				// CREATE NEW MARKERS

				function toMarkers(imageMarkers) {
					return _.map(imageMarkers, function (imageMarker) {
						var marker = createMarker(imageMarker, $scope);
						marker.on('click', function (event) {
							$scope.$apply(function () {
								$scope.markerClickHandler({marker: marker});
							});
						});
						return marker;
					});
				}

				// MARKERS AS FLAT ARRAY => NO LAYERS
				if (angular.isArray(imageMarkers)) {
					$scope.hasLayers = false;

					var leafletMarkers = toMarkers(imageMarkers);
					_.each(leafletMarkers, function (leafletMarker) {
						leafletMarker.addTo(map);
					});

					$scope.leafletMarkers = leafletMarkers;
				}
				// MARKERS AS OBJECT => LAYERS WITH KEY AS NAME
				else if (angular.isObject(imageMarkers)) {
					$scope.hasLayers = true;

					var markerLayers = {};

					_.each(imageMarkers, function (imageMarkers, layerName) {
						markerLayers[layerName] = L.layerGroup();

						var leafletMarkers = toMarkers(imageMarkers);
						_.each(leafletMarkers, function (leafletMarker) {
							markerLayers[layerName].addLayer(leafletMarker);
						});
					});

					// show all layers
					_.each(markerLayers, function (layerGroup) {
						layerGroup.addTo(map);
					});
					// add control
					$scope.layerControl = L.control.layers({}, markerLayers);
					$scope.layerControl.addTo(map);

					$scope.leafletMarkerLayers = markerLayers;
				}
			}, true);

		}]
	};
};
imageMapModule.directive('imageMap', imageMapDirective);