var chalkUpServices = angular.module('chalkUpServices', ['ngResource']);

// GYM SERVICE
var Gym = function ($resource) {
	return $resource('/gyms/:gymId',
		{
			gymId: '@id'
		},
		{
			boulders: { method: 'GET', url: '/gyms/:gymId/boulders', isArray: true }
		});
};
Gym.$inject = ['$resource'];
chalkUpServices.factory('Gym', Gym);


// BOULDER SERVICE
var Boulder = function ($resource) {
	return $resource('/boulders/:boulderId');
};
Boulder.$inject = ['$resource'];
chalkUpServices.factory('Boulder', Boulder);


// FLOOR PLAN SERVICE
var FloorPlan = function () {
	// TODO: I should probably not interact with the DOM here...
	var init = function (floorPlan, $mapDiv) {
		var factor = 1.35;

		var map = L.map($mapDiv[0], {
			maxZoom: 10,
			crs: L.CRS.Simple
		});

		L.control.fullscreen({
			position: 'topleft',
			forceSeparateButton: true
		}).addTo(map);


		map.toLatLng = function (point) {
			point = L.point(point);
			return map.unproject([point.x * factor, point.y * factor], map.getMaxZoom())
		};

		map.toPoint = function (latLng) {
			var point = map.project(latLng, map.getMaxZoom());
			point.x /= factor;
			point.y /= factor;
			return point;
		};

		var width = floorPlan.img.widthInPx;
		var height = floorPlan.img.heightInPx;
		var src = floorPlan.img.url;

		var southWest = map.toLatLng([0, height]);
		var northEast = map.toLatLng([width, 0]);
		var imageBounds = new L.LatLngBounds(southWest, northEast);
		map.setMaxBounds(imageBounds);
		map.fitBounds(imageBounds);

		L.imageOverlay(src, imageBounds, {
			minZoom: map.getZoom()
		}).addTo(map);


		function _getIcon(marker) {
			return $(marker._icon);
		}

		function createMarker(p, options) {
			var myIcon = L.divIcon({
				className: 'boulder-marker',
				html: '&#xf172;',
				iconSize: undefined // set in CSS
			});

			var BoulderMarker = L.Marker.extend({
				getPoint: function () {
					var latLng = marker.getLatLng();
					return map.toPoint(latLng);
				},
				options: {
					icon: myIcon,
					riseOnHover: true,
					color: 'rgb(0,0,0)'
				},
				setColor: function (newColor) {
					L.Util.setOptions(this, {color: newColor});
					colorIcon.call(this);
				}
			});

			var marker = new BoulderMarker(map.toLatLng(p), options);


			function colorIcon() {
				// color marker according to color option
				var color = this.options.color;
				var $icon = _getIcon(this);
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

			marker.on('add', colorIcon);

			return marker;
		}

		function addMarker(e, options) {
			var p = map.toPoint(e.latlng);
			var marker = createMarker(p, options);
			marker.addTo(map);
			return marker;
		}

		function mark(boulder) {
			var marker = createMarker([boulder.location.x * width, boulder.location.y * height], {
				color: boulder.color
			});

			return marker;
		}


		function markerAscentPopup(marker, boulder, getAscentStyle, setAscentStyle) {
			var popupContent =
				'<p><small>ID: ' + boulder.id + '</small>, ' +
					'<small>grade: ' + boulder.initialGrade.readable + '</small></p>' +
					'<input id="flash" type="checkbox" class="style-select" value="flash">' +
					'<label for="flash">flash</label>' +
					'<input id="top" type="checkbox" class="style-select" value="top">' +
					'<label for="top">top</label>';

			marker.bindPopup(
				popupContent, {
					offset: [0, -30],
					closeButton: false
				});

			marker.on('popupopen', function (e) {
				var $popupContent = $(e.popup._contentNode);

				// set checkboxes according to current style
				var style = getAscentStyle(boulder);
				$popupContent.find('input:checkbox.style-select[value=' + style + ']').prop('checked', true);

				// set click handlers that change style
				$popupContent.find('input:checkbox.style-select').click(function () {
					if ($(this).is(':checked')) {
						$popupContent.find('input:checkbox.style-select').not(this).prop('checked', false);
					}
					var style = $popupContent.find('input:checkbox.style-select:checked').val();
					if (!style)
						style = "none";
					setAscentStyle(boulder, style);
				});
			});
		};


		function groupByColor(markers) {
			var layerGroups = {};

			$.each(markers, function (index, marker) {
				// TODO: use proper locale here
				var colorName = marker.options.color.germanName;
				if (!(colorName in layerGroups)) {
					layerGroups[colorName] = L.layerGroup();
				}
				layerGroups[ colorName].addLayer(marker);
			});

			return layerGroups;
		}

		function cursor(cursor) {
			$mapDiv.css({cursor: cursor});
		}

		function destroy() {
			map.remove();
		}

		return {
			width: width,
			height: height,
			map: map,
			addMarker: addMarker,
			mark: mark,
			markerAscentPopup: markerAscentPopup,
			groupByColor: groupByColor,
			cursor: cursor,
			destroy: destroy
		};
	};

	return {
		init: init
	};
}
FloorPlan.$inject = [];
chalkUpServices.factory('FloorPlan', FloorPlan);

// USER SERVICE
var User = function ($resource) {
	return $resource('/users/:userId?format=json',
		{
			userId: '@id'
		},
		{
			statistics: { method: 'GET', url: '/users/:userId/statistics?format=json', isArray: true }
		}
	);
};
User.$inject = ['$resource'];
chalkUpServices.factory('User', User);


// BOULDERING SESSION SERVICE
var BoulderingSession = function ($resource) {
	return $resource('/sessions/:sessionId?format=json');
};
BoulderingSession.$inject = ['$resource'];
chalkUpServices.factory('BoulderingSession', BoulderingSession);


// GRADES SERVICE
var Grades = function ($resource) {
	return $resource('/grades?format=json');
};
Grades.$inject = ['$resource'];
chalkUpServices.factory('Grades', Grades);