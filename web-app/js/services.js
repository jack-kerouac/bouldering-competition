var chalkUpServices = angular.module('chalkUpServices', ['ngResource']);

// GYM SERVICE
var gym = function ($resource) {
	return $resource('/api/gyms/:gymId');
};
gym.$inject = ['$resource'];
chalkUpServices.factory('gym', gym);

// BOULDER SERVICE
var boulder = function ($resource) {
	return $resource('/api/gyms/:gymId/boulders/:boulderId');
};
boulder.$inject = ['$resource'];
chalkUpServices.factory('boulder', boulder);
