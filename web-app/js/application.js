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
});