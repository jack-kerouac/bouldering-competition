if (typeof jQuery !== 'undefined') {
	(function ($) {
		$('#spinner').ajaxStart(function () {
			$(this).fadeIn();
		}).ajaxStop(function () {
				$(this).fadeOut();
			});
	})(jQuery);
}


$(document).foundation();

$(function () {
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

			var markers = fp.markBoulders($boulders.find('li'));

			$.each(markers, function(i, marker) {
				marker.on('click', function() {
					// TODO: remove chosen class from other markers
					$(marker._icon).addClass('chosen');

					var $boulder = marker.options.$boulder;
					$boulder.find('input[type="radio"]').click();
				});
			});

		}


		if ($('#create-boulder-page').exists()) {
			var marker = fp.addMarker(100, 200, {draggable: true});

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
				marker.setColor(colors);
			});

			var colors = getSelectedColor($select);
			marker.setColor(colors);


			function updatePositionFields(point) {
				$('#x').val((point.x / fp.width).toLocaleString(locale));
				$('#y').val((point.y / fp.height).toLocaleString(locale));
			}

			marker.on('drag', function (e) {
				var marker = e.target;
				var point = marker.getPoint();
				updatePositionFields(point);
			});

			updatePositionFields(marker.getPoint());
		}
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
});


function getColors($boulder) {
	var primary = $boulder.data('color-primary');
	var secondary = $boulder.data('color-secondary');
	if (secondary)
		return [primary, secondary];
	else
		return primary;
}


function initFloorPlan($floorPlanImg) {
	var width = parseInt($floorPlanImg.attr('width'));
	var height = parseInt($floorPlanImg.attr('height'));
	var src = $floorPlanImg.attr('src');

	$floorPlanImg.hide();
	var $mapDiv = $('<div class="map"></div>').insertAfter($floorPlanImg);

	var map = L.map($mapDiv[0], {
		maxZoom: 10,
		crs: L.CRS.Simple
	});

	map.toLatLng = function (x, y) {
		return map.unproject([x, y], map.getMaxZoom())
	};

	var southWest = map.toLatLng(0, height);
	var northEast = map.toLatLng(width, 0);
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

	function createMarker(x, y, options) {
		var myIcon = L.divIcon({
			className: 'boulder-marker',
			html: '&#xf172;',
			iconSize: undefined // set in CSS
		});

		var BoulderMarker = L.Marker.extend({
			getPoint: function () {
				return map.project(marker.getLatLng(), map.getMaxZoom());
			},
			options: {
				icon: myIcon,
				riseOnHover: true,
				color: 'rgb(0,0,0)'
			},
			setColor: function(newColor) {
				L.Util.setOptions(this, {color: newColor});
				colorIcon.call(this);
			}
		});

		var marker = new BoulderMarker(map.toLatLng(x, y), options);


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
					background: '-webkit-linear-gradient(' + color[0] + ', ' + color[1] + ')',
					'-webkit-background-clip': 'text',
					'-webkit-text-fill-color': 'transparent'
				});
			}
		};

		marker.on('add', colorIcon);

		return marker;
	}

	function addMarker(x, y, options) {
		var marker = createMarker(x, y, options);
		marker.addTo(map);
		return marker;
	}

	function connectMarker(marker, $boulder) {
		var boulderId = $boulder.attr('id');
		if (!boulderId)
			throw new Error('boulder ' + $boulder + ' has no ID!');

		marker.options.$boulder = $boulder;

		marker.bindPopup(
			'current grade: ' + $boulder.data('current-font-grade'),
			{
				offset: [0, -30]
			});
	}

	function markBoulders($boulders) {
		var markers = [];

		var layerGroups = {};

		$boulders.each(function () {
			var b = $(this);
			var marker = createMarker(b.data('x'), b.data('y'), {
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
		markBoulders: markBoulders
	};
}


