var chalkUpApp = angular.module('chalkUpApp', ['chalkUpServices', 'chalkUpControllers']);

$(document).foundation();

window.initChalkUp = function () {

	jQuery.fn.exist = function () {
		return jQuery(this).length != 0;
	};
	jQuery.fn.exists = function () {
		return jQuery(this).length != 0;
	};


	L.Icon.Default.imagePath = '/images/leaflet';
	var $floorPlan = $('img.floor-plan');
	if ($floorPlan.exists()) {
		var fp = initFloorPlan($floorPlan);

		var $boulders = $('.boulder-location-map ul.boulders');
		if ($boulders.exist()) {
			$boulders.hide();

			var markers = fp.markBoulders($boulders.children('li'));

			if ($('#log-session-page').exists()) {
				$.each(markers, function (i, marker) {
					function setAscentStyle($boulder, style) {
						$boulder.find('input:radio[value=' + style + ']').prop('checked', true);
					}

					function getAscentStyle($boulder) {
						return $boulder.find('input:radio:checked').val();
					}

					var popupContent =
						'<p><small>ID: ' + marker.options.$boulder.data('id') + '</small>, ' +
							'<small>grade: ' + marker.options.$boulder.data('initial-grade') + '</small></p>' +
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
						var $boulder = e.target.options.$boulder;

						// set checkboxes according to current style
						var style = getAscentStyle($boulder);
						$popupContent.find('input:checkbox.style-select[value=' + style + ']').prop('checked', true);

						// set click handlers that change style
						$popupContent.find('input:checkbox.style-select').click(function () {
							if ($(this).is(':checked')) {
								$popupContent.find('input:checkbox.style-select').not(this).prop('checked', false);
							}
							var style = $popupContent.find('input:checkbox.style-select:checked').val();
							if (!style)
								style = "none";
							setAscentStyle($boulder, style);
						});
					});
				});
			}
			if ($('#list-ascents-page').exists()) {
				$.each(markers, function (i, marker) {
					var $boulder = marker.options.$boulder;
					var popupContent = $boulder.html();

					marker.bindPopup(
						popupContent, {
							offset: [0, -30],
							closeButton: false
						});
				});
			}
		}

		if ($('#create-boulder-page').exists()) {
			fp.$mapDiv.css({cursor: 'crossHair'});

			var markers = [];

			function updatePositionFields(i, point) {
				$('input[name="coordinates[' + i + '].x"]').val((point.x / fp.width).toLocaleString(locale));
				$('input[name="coordinates[' + i + '].y"]').val((point.y / fp.height).toLocaleString(locale));
			}

			function getSelectedColor($select) {
				var val = $select.val();
				var $opt = $select.find('option[value="' + val + '"]');
				var primary = $opt.data('color-primary');
				var secondary = $opt.data('color-secondary');
				return getColors($opt);
			};

			var $select = $('select[name="color"]');
			$select.change(function () {
				var colors = getSelectedColor($(this));
				$.each(markers, function () {
					this.setColor(colors);
				});
			});

			fp.map.on('click', function (e) {
				var marker = fp.addMarker(e, {draggable: true});
				// update colors
				var colors = getSelectedColor($select);
				marker.setColor(colors);

				var markerNumber = markers.length;

				// add pos fields
				$('ul.coordinates').append('<li>' +
					'<input type="hidden" name="coordinates[' + markerNumber + '].x"/>' +
					'<input type="hidden" name="coordinates[' + markerNumber + '].y"/>' +
					'</li>')

				// update pos
				marker.on('dragend', function (e) {
					var marker = e.target;
					var point = marker.getPoint();
					updatePositionFields(markerNumber, point);
				});
				updatePositionFields(markerNumber, marker.getPoint());


				markers.push(marker);
			});
		}
	}


	if ($('#list-sessions-page').exists()) {
		var currentGradeData = [];
		var ascentCountData = [];
		var scoreData = [];

		var registrationDate = Date.parse($('.about-boulderer time').attr('datetime'));
		var initialGrade = parseFloat($('.about-boulderer .grade').text().replace(',', '.'));
		currentGradeData.push([registrationDate, initialGrade]);

		$('#sessions tbody tr').each(function (e) {
			var date = Date.parse($(this).find('time').attr('datetime'));
			var grade = parseFloat($(this).find('.grade').text().replace(',', '.'));
			currentGradeData.push([date, grade]);
			var ascentCount = parseInt($(this).find('.ascent-count').text());
			ascentCountData.push([date, ascentCount]);
			var score = parseInt($(this).find('.score').text());
			scoreData.push([date, score]);
		});

		var gradeColor = 'rgb(203,75,75)';
		var ascentCountColor = 'rgb(175,216,248)';
		var scoreColor = 'rgb(237,194,64)';

		var grades = $(".chart").data('grades')
		$.plot($(".chart"), [
			{ data: currentGradeData, label: $('#sessions thead th.grade').text(), color: gradeColor, yaxis: 1 },
			{ data: ascentCountData, label: $('#sessions thead th.ascent-count').text(), color: ascentCountColor, yaxis: 2 }
			// TODO: what about score?
//			{ data: scoreData, label: $('#sessions thead th.score').text(), color: scoreColor, yaxis: 3}
		], {
			xaxis: {
				mode: "time",
				tickSize: [1, "day"]
			},
			yaxes: [
				{
					min: 0.3,
					max: 0.9,
					ticks: grades,
					font: { color: gradeColor }
				},
				{
					tickDecimals: 0,
					position: 'right',
					font: { color: ascentCountColor }
				},
				{
					tickDecimals: 0,
					position: 'right',
					font: { color: scoreColor }
				}
			],
			series: {
				lines: { show: true, fill: false },
				points: { show: true, fill: false }
			}
		});
	}

	function displayChart(mu, sigma) {
		var dd = [];

		function density(x) {
			return 1 / (sigma * Math.sqrt(2 * Math.PI)) * Math.exp(-(x - mu) * (x - mu) / (2 * sigma * sigma));
		}

		for (var i = 0.0; i <= 1.0; i += 0.005)
			dd.push([i, density(i)]);

		var ticks = $("#chartModal .chart").data('ticks')
		$.plot($("#chartModal .chart"), [ dd ], {
			xaxis: {
				min: 0,
				max: 1.0,
				ticks: ticks
			},
			grid: {
				markings: [
					{ xaxis: { from: mu - 1.645 * sigma, to: mu - 1.645 * sigma}, lineWidth: 2, color: "#ee9999" },
					{ xaxis: { from: mu + 1.645 * sigma, to: mu + 1.645 * sigma}, lineWidth: 2, color: "#ee9999" },
					{ xaxis: { from: mu, to: mu}, lineWidth: 2, color: "#9999ee" }
				]
			}
		});

		$('#chartModal').foundation('reveal', 'open');
	}

	$('#show-meta-page table.boulders tbody tr').css({cursor: 'pointer'});
	$('#show-meta-page table.boulders tbody tr').click(function (e) {
		var mu = parseFloat($(this).find('td:nth-child(3) span:nth-child(1)').text().replace(',', '.'));
		var sigma = Math.sqrt(parseFloat($(this).find('td:nth-child(4)').text().replace(',', '.')));
		displayChart(mu, sigma);
	});

	$('#show-meta-page table.users tbody tr').css({cursor: 'pointer'});
	$('#show-meta-page table.users tbody tr').click(function (e) {
		var mu = parseFloat($(this).find('td:nth-child(4) span:nth-child(1)').text().replace(',', '.'));
		var sigma = Math.sqrt(parseFloat($(this).find('td:nth-child(5)').text().replace(',', '.')));
		displayChart(mu, sigma);
	});
}


function getColors($boulder) {
	var primary = $boulder.data('color-primary');
	var secondary = $boulder.data('color-secondary');
	if (secondary)
		return [primary, secondary];
	else
		return primary;
}


function initFloorPlan($floorPlanImg) {
	var factor = 1.35;
	var width = parseInt($floorPlanImg.attr('width'));
	var height = parseInt($floorPlanImg.attr('height'));
	var src = $floorPlanImg.attr('src');

	$floorPlanImg.hide();
	var $mapDiv = $('<div class="map"></div>').insertAfter($floorPlanImg);

	var map = L.map($mapDiv[0], {
		maxZoom: 10,
		crs: L.CRS.Simple
	});

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

	var southWest = map.toLatLng([0, height]);
	var northEast = map.toLatLng([width, 0]);
	var imageBounds = new L.LatLngBounds(southWest, northEast);
	map.setMaxBounds(imageBounds);
	map.fitBounds(imageBounds);

	L.imageOverlay(src, imageBounds, {
		attribution: 'Boulderwelt',
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
			if (typeof color === 'string') {
				$icon.css({
					color: color,
					background: '',
					'-webkit-background-clip': '',
					'-webkit-text-fill-color': ''
				});
			}
			else {
				// use text gradient for two colored boulders
				$icon.css({
					color: 'black',
					background: 'linear-gradient(' + color[0] + ', ' + color[1] + ')',
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

	function connectMarker(marker, $boulder) {
		var boulderId = $boulder.attr('id');
		if (!boulderId)
			throw new Error('boulder ' + $boulder + ' has no ID!');

		marker.options.$boulder = $boulder;
	}

	function markBoulders($boulders) {
		var markers = [];

		var layerGroups = {};

		$boulders.each(function () {
			var b = $(this);
			var marker = createMarker([b.data('x'), b.data('y')], {
				color: getColors(b)
			});

			connectMarker(marker, b);

			var color = b.data('color');
			if (!(color in layerGroups)) {
				layerGroups[color] = L.layerGroup();
			}
			layerGroups[color].addLayer(marker);
			markers.push(marker);

			b.data('marker', marker);
		});

		// show all markers
		for (var layerGroup in layerGroups) {
			layerGroups[layerGroup].addTo(map);
		}

		L.control.layers({}, layerGroups).addTo(map);

		return markers;
	}

	return {
		width: width,
		height: height,
		map: map,
		addMarker: addMarker,
		markBoulders: markBoulders,
		$mapDiv: $mapDiv
	};
}


