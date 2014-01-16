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
			// form a jquery object out of an array of HtmlElements
			var $markers = $($.map(markers, function (marker) {
				return fp.getIcon(marker)[0];
			}));
			$markers.click(function () {
				var $marker = $(this);
				$markers.removeClass('chosen');
				$marker.addClass('chosen');

				var $boulder = $($marker.attr('rel'));
				$boulder.find('input[type="radio"]').click();
			});
		}


		if ($('#create-boulder-page').exists()) {
			var marker = fp.addMarker(100, 200, {draggable: true});

			function updateColor($select) {
				var val = $select.val();
				var $opt = $select.find('option[value="' + val + '"]');
				var primary = $opt.data('color-primary');
				var secondary = $opt.data('color-secondary');
				fp.colorMarker(marker, primary, secondary);
			};
			var $select = $('select[name="color"]');
			$select.change(function () {
				updateColor($(this));
			});

			updateColor($select);

			function updatePositionFields(point) {
				$('#x').val(point.x / fp.width);
				$('#y').val(point.y / fp.height);
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
		var mu = parseFloat($(this).find('td:nth-child(3) span:nth-child(1)').text());
		var sigma = Math.sqrt(parseFloat($(this).find('td:nth-child(4)').text()));
		displayChart(mu, sigma);
	});

	$('#show-meta-page table.users tbody tr').css({cursor: 'pointer'});
	$('#show-meta-page table.users tbody tr').click(function (e) {
		var mu = parseFloat($(this).find('td:nth-child(4) span:nth-child(1)').text());
		var sigma = Math.sqrt(parseFloat($(this).find('td:nth-child(5)').text()));
		displayChart(mu, sigma);
	});
});

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

	function getIcon(marker) {
		return $(marker._icon);
	}

	function colorMarker(marker, primary, secondary) {
		var $icon = getIcon(marker);
		if (secondary) {
			/* use text gradient for two colored boulders */
			$icon.css({
				color: 'black',
				background: '-webkit-linear-gradient(' + primary + ', ' + secondary + ')',
				'-webkit-background-clip': 'text',
				'-webkit-text-fill-color': 'transparent'
			});
		}
		else {
			$icon.css({
				color: primary,
				background: '',
				'-webkit-background-clip': '',
				'-webkit-text-fill-color': ''
			});
		}
	}

	function addMarker(x, y, options) {
		var PointMarker = L.Marker.extend({
			getPoint: function () {
				return map.project(marker.getLatLng(), map.getMaxZoom());
			}
		});

		var myIcon = L.divIcon({
			className: 'boulder-marker',
			html: '&#xf172;',
			iconSize: undefined // set in CSS
		});

		var defaults = {
			icon: myIcon,
			riseOnHover: true
		};
		if (typeof options === "undefined")
			options = {};
		var _options = $.extend({}, defaults, options);

		var marker = new PointMarker(map.toLatLng(x, y), _options).addTo(map);

		return marker;
	}

	function connectMarker(marker, $boulder) {
		if (!$boulder.attr('id')) {
			throw new Error('boulder ' + $boulder + ' has no ID!');
		}
		var $icon = getIcon(marker);
		$icon.attr('rel', '#' + $boulder.attr('id'));

		marker.bindPopup(
			'current grade: ' + $boulder.data('current-font-grade'),
			{
				offset: [0, -30]
			});
	}

	function markBoulders($boulders) {
		var markers = new Array();

		$boulders.each(function () {
			var b = $(this);
			var marker = addMarker(b.data('x'), b.data('y'));
			colorMarker(marker, b.data('color-primary'), b.data('color-secondary'));
			connectMarker(marker, b);
			markers.push(marker);
		});
		return markers;
	}

	return {
		width: width,
		height: height,
		map: map,
		colorMarker: colorMarker,
		addMarker: addMarker,
		connectMarker: connectMarker,
		markBoulders: markBoulders,
		getIcon: getIcon
	};
}


