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
	var attr = "readonly"

	if ($("#top").is(":checked"))
		$("#tries").removeAttr(attr);
	else
		$("#tries").attr(attr, true);

	$("#flash, #top").change(function () {
		if ($("#top").is(":checked")) {
			$("#tries").removeAttr(attr);
			$("#tries").focus();
		}
		else {
			$("#tries").attr(attr, true);
		}
	});




	document.fp = floorPlan($('.boulder-location-map .floor-plan'));
	document.fp.addAllBoulders($('.boulder-location-map li'));

	/* this is for the create boulder page *//*
	$('#create-boulder-page img.floor-plan').click(function (e) {
		var offset = $(this).offset();
		var width = $(this).width();
		var height = $(this).height();

		$("#create-boulder-page input[name='x']").val((e.pageX - offset.left) / width);
		$("#create-boulder-page input[name='y']").val((e.pageY - offset.top) / height);
	});
	initBoulderLocationMap($('#create-boulder-page .boulder-location-map'), 'crosshair', false);


	*//* this is for the create ascent page *//*
	initBoulderLocationMap($('#create-ascent-page .boulder-location-map'), 'move', true);*/


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
	})

	$('#show-meta-page table.users tbody tr').css({cursor: 'pointer'});
	$('#show-meta-page table.users tbody tr').click(function (e) {
		var mu = parseFloat($(this).find('td:nth-child(4) span:nth-child(1)').text());
		var sigma = Math.sqrt(parseFloat($(this).find('td:nth-child(5)').text()));
		displayChart(mu, sigma);
	})
});


function floorPlan($floorPlan) {
	var width = $floorPlan.data('width');
	var height = $floorPlan.data('height');

	var map = L.map($floorPlan[0], {
		maxZoom: 10,
		crs: L.CRS.Simple
	});

	function toLatLng(x, y) {
		return map.unproject([x, y], map.getMaxZoom())
	}

	var southWest = toLatLng(0, height);
	var northEast = toLatLng(width, 0);
	var imageBounds = new L.LatLngBounds(southWest, northEast);
	map.setMaxBounds(imageBounds);
	map.fitBounds(imageBounds);

	var imageUrl = $floorPlan.data('src')

	L.imageOverlay(imageUrl, imageBounds, {
		attribution: 'Boulderwelt',
		minZoom: map.getZoom()
	}).addTo(map);

	function addBoulder(x, y, primary, secondary, $marker) {
		var myIcon = L.divIcon({
			className: 'boulder-marker',
			html: $marker.html()
		});

		$marker.remove();

		var marker = L.marker(toLatLng(width * x, height * y), {icon: myIcon}).addTo(map);

		$(marker._icon);

		if (secondary) {
			/* use text gradient for two colored boulders */
			$(marker._icon).find('label').css({
				background: '-webkit-linear-gradient(' + primary + ', ' + secondary + ')',
				'-webkit-background-clip': 'text',
				'-webkit-text-fill-color': 'transparent'
			});
		}
		else {
			$(marker._icon).find('label').css({
				color: primary
			});
		}

		var $radioButton = $(marker._icon).find("input[type='radio']");
		$radioButton.change(function (e) {
			$radioButton.parent('.boulder-marker').siblings('.boulder-marker').removeClass('chosen');
			$(this).parent('.boulder-marker').addClass('chosen');
		});
	}

	return {
		map: map,
		addBoulder: addBoulder,
		addAllBoulders: function($boulders) {
			$boulders.each(function (e, t) {
				var b = $(this);
				addBoulder(b.data('x'), b.data('y'), b.data('color-primary'), b.data('color-secondary'), b);
			});
		}
	}
}