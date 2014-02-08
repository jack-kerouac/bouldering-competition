var boulderMetaModule = angular.module('boulderMeta', []);

var boulderMetaDirective = function () {

	return {
		restrict: 'E',
		templateUrl: '/templates/boulderMeta.html',
		scope: {
			boulder: '=',
			ascents: '=',
			// TODO: do not require users, but replace boulderer (ID) in ascents by user data
			users: '='
		}
	};
};
boulderMetaModule.directive('boulderMeta', boulderMetaDirective);