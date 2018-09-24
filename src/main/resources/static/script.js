var bnpApp = angular.module('bnpApp', []);

bnpApp.controller('MainController', function MainController($scope, $http) {

	var onUserComplete = function(response) {
		$scope.clients = response.data;
	};

	$http.get("/api/clients").then(onUserComplete);

});
