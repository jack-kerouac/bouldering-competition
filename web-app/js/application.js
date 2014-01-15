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
		var map = initFloorPlan($floorPlan);

		var $boulders = $('.boulder-location-map ul.boulders');
		if ($boulders.exist()) {
			$boulders.hide();

			var $markers = markBoulders($boulders.find('li'), map);
			$markers.click(function () {
				var $marker = $(this);
				$markers.removeClass('chosen');
				$marker.addClass('chosen');

				var $boulder = $($marker.attr('rel'));
				$boulder.find('input[type="radio"]').click();
			});
		}


		if ($('#create-boulder-page').exists()) {
			var myIcon = L.divIcon({
				className: 'boulder-marker',
				html: '&#xf172;',
				iconSize: undefined
			});

			var marker = L.marker(map.toLatLng(500, 500), {
				icon: myIcon,
				draggable: true
			}).addTo(map);

			$('select[name="color"]').change(function() {
				var val = $(this).val();
				var $opt = $(this).find('option[value="' + val + '"]');
				var primary = $opt.data('color-primary');
				var secondary = $opt.data('color-secondary');
				colorMarker($(marker._icon), primary, secondary);
			});
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

	return map;
}

function colorMarker($marker, primary, secondary) {
	if (secondary) {
		/* use text gradient for two colored boulders */
		$marker.css({
			color: 'black',
			background: '-webkit-linear-gradient(' + primary + ', ' + secondary + ')',
			'-webkit-background-clip': 'text',
			'-webkit-text-fill-color': 'transparent'
		});
	}
	else {
		$marker.css({
			color: primary,
			background: '',
			'-webkit-background-clip': '',
			'-webkit-text-fill-color': ''
		});
	}
}

function markBoulders($boulders, map) {

	function addBoulderMarker(x, y, primary, secondary, $boulder) {
		var myIcon = L.divIcon({
			className: 'boulder-marker',
			html: '&#xf172;',
			iconSize: undefined // set in CSS
		});

		var marker = L.marker(map.toLatLng(x, y), {
			icon: myIcon,
			riseOnHover: true
		}).addTo(map);

		var $icon = $(marker._icon);

		colorMarker($icon, primary, secondary);


		if (!$boulder.attr('id')) {
			throw new Error('boulder ' + $boulder + ' has no ID!');
		}
		$icon.attr('rel', '#' + $boulder.attr('id'));
	}

	$boulders.each(function () {
		var b = $(this);
		addBoulderMarker(b.data('x'), b.data('y'), b.data('color-primary'), b.data('color-secondary'), b);
	});

	return $(map._container).find('.boulder-marker');
}