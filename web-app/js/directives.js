var chalkUpDirectives = angular.module('chalkUpDirectives', []);


var flotChartDirective = function () {
	return {
		restrict: 'A',
		link: function (scope, elem, attrs) {
			var chart = null;

			// If the options change replot
			scope.$watch(attrs.options, function (options) {
				chart = $.plot(elem, scope.$eval(attrs.data), options);
				elem.show();
				chart.setData(scope.$eval(attrs.data));
				chart.setupGrid();
				chart.draw();
			}, true);

			// If the data changes somehow, update it in the chart
			scope.$watch(attrs.data, function (data) {
				chart.setData(data);
				chart.setupGrid();
				chart.draw();
			}, true);
		}
	};
};
chalkUpDirectives.directive('flotChart', flotChartDirective);