if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}


$(document).foundation();

$(function() {
	var attr = "readonly"

	if($("#top").is(":checked"))
		$("#tries").removeAttr(attr);
	else
		$("#tries").attr(attr, true);

	$("#flash, #top").change(function() {
		if($("#top").is(":checked")){
			$("#tries").removeAttr(attr);
			$("#tries").focus();
		}
		else {
			$("#tries").attr(attr, true);
		}
	});

	initBoulderLocationMap($('.boulder-location-map'), false);

});

function initBoulderLocationMap($boulderLocationMap, makeZoomable) {
	var $boulderMarker = $boulderLocationMap.find('input[type=radio]');

	if (makeZoomable) {
		var $zoomElem = $boulderLocationMap;

		function getScale() {
			return $zoomElem.data('scale');
		}

		function setScale(scale) {
			$zoomElem.data('scale', scale);
		}

		$zoomElem.panzoom({
			minScale: 1.0,
			maxScale: 2.0,
			contain: 'invert',
			increment: 1.0
		});

		setScale(1.0);

		$zoomElem.on('dblclick', function (e) {
			var opts = $(this).panzoom('option');
			if (getScale() == opts.maxScale) {
				$(this).panzoom('resetZoom');
			}
			else {
				$(this).panzoom('zoom', false, {
					focal: e
				});
			}
		});

		$zoomElem.on('panzoomzoom', function (e, panzoom, scale, opts) {
			setScale(scale)
		});

		$zoomElem.on('panzoomstart', function (e, panzoom) {
			$boulderMarker.hide();
		});
		$zoomElem.on('panzoomend', function (e, panzoom) {
			$boulderMarker.show();
		});
		$zoomElem.on('panzoomzoom', function (e, panzoom, scale) {
			resizeBoulderMarkers(scale);
		});

	}

	function resizeBoulderMarkers(scale) {
		var transformString = 'scale(' + 1 / scale + ', ' + 1 / scale + ')';
		$boulderMarker.each(function (e, t) {
			$(t).css({
				transform: transformString,
				'-webkit-transform': transformString
			});
		});
	}

	function positionBoulderMarkers() {
		$boulderMarker.each(function (e, t) {
			var blmDisplayW = $boulderLocationMap.width();
			var blmW = $boulderLocationMap.data('width');
			var blmDisplayH = $boulderLocationMap.height();
			var blmH = $boulderLocationMap.data('height');
			var x = $(t).data('x');
			var y = $(t).data('y');

			var markerOffsetX = -7;
			var markerOffsetY = -42;
			$(t).css({
				left: x / blmW * blmDisplayW + markerOffsetX,
				top: y / blmH * blmDisplayH + markerOffsetY
			});
		});
	}

	positionBoulderMarkers();

	function colorMarkers() {
		$boulderMarker.each(function (e, t) {
			$(t).css({
				color: $(t).data('grade')
			});
		});
	}
	colorMarkers();


	$boulderMarker.click(function(e) {
		console.log('testtt');
	});
}
