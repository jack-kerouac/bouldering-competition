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


	/* this is for the create boulder page */
	$('#create-boulder-page img.floor-plan').click(function (e) {
		var offset = $(this).offset();
		var width = $(this).width();
		var height = $(this).height();

		$("#create-boulder-page input[name='x']").val((e.pageX - offset.left) / width);
		$("#create-boulder-page input[name='y']").val((e.pageY - offset.top) / height);
	});
	initBoulderLocationMap($('#create-boulder-page .boulder-location-map'), 'crosshair', false);


	/* this is for the create ascent page */
	initBoulderLocationMap($('#create-ascent-page .boulder-location-map'), 'move', true);


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

function initBoulderLocationMap($boulderLocationMap, cursor, makeZoomable) {
	var $boulderMarker = $boulderLocationMap.find('li');

	if (makeZoomable) {
		var $zoomElem = $boulderLocationMap;

		function currentScale() {
			var matrix = $zoomElem.panzoom('getMatrix');
			return matrix[0];
		}

		$zoomElem.panzoom({
			minScale: 1.0,
			maxScale: 4.0,
			contain: 'invert',
			increment: 1.0,
			cursor: cursor
		});

		$zoomElem.on('dblclick', function (e) {
			var animate = true;

			var opts = $(this).panzoom('option');
			if (currentScale() == opts.maxScale) {
				$(this).panzoom('resetZoom', animate);
			}
			else {
				$(this).panzoom('zoom', false, {
					focal: e,
					animate: animate
				});
			}
		});

		/* without this, a tap on a label (the marker) does not select the input field. See FAQ at https://github.com/timmywil/jquery.panzoom */
		$zoomElem.find('label').on('touchstart', function (e) {
			e.stopImmediatePropagation();
		});

		$zoomElem.on('panzoomzoom', function (e, panzoom, scale, opts) {
			resizeBoulderMarkers(scale, opts);
		});


		function resizeBoulderMarkers(scale, opts) {
			var transformString = 'scale(' + 1 / scale + ', ' + 1 / scale + ')';
			var css = {};
			css['transform'] = transformString;
			css['webkit-transform'] = transformString;
			var transition;
			if (opts.animate)
				transition = '-webkit-transform ' + opts.duration + 'ms ' + opts.easing;
			else
				transition = 'none';
			css['transition'] = transition;
			css['-webkit-transition'] = transition;
			$boulderMarker.css(css);
		}

	}
	else {
		$boulderLocationMap.css({cursor: cursor});
	}

	function positionBoulderMarkers() {
		$boulderMarker.each(function (e, t) {
			var x = $(t).data('x');
			var y = $(t).data('y');

			$(t).css({
				left: x * 100 + "%",
				top: y * 100 + "%"
			});
		});
	}

	positionBoulderMarkers();

	function colorMarkers() {
		$boulderMarker.each(function (e, t) {
			var primary = $(t).data('color-primary');
			var secondary = $(t).data('color-secondary');
			if (secondary) {
				/* use text gradient for two colored boulders */
				$(t).find('label').css({
					background: '-webkit-linear-gradient(' + primary + ', ' + secondary + ')',
					'-webkit-background-clip': 'text',
					'-webkit-text-fill-color': 'transparent'
				});
			}
			else {
				$(t).find('label').css({
					color: $(t).data('color-primary')
				});
			}
		});
	}

	colorMarkers();

	var $radioButtons = $boulderMarker.find("input[type='radio']");
	$radioButtons.change(function (e) {
		$radioButtons.parent('li').removeClass('chosen');
		$(this).parent('li').addClass('chosen');
	});
}
