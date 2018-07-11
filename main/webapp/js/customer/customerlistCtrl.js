resourceApp.controller('customerlistCtrl',['$scope','$document','RAService',function($scope,$document,RAService){
	$scope.$on('$viewContentLoaded', function () {
		$scope.getRegister();
	})

	
	
	$scope.getRegister = function() {
		$scope.id=localStorage.getItem('registrationId');
		RAService.getRegistrationById($scope.id).then(
				function(data) {
					$scope.registration = data.result;
					console.log($scope.registration);
					
				}, function(err) {
					if (err) {
						$scope.errorMessage = err;
					}
				})
	}
	
	$scope.showMe = false;
	$scope.msalist=function(){
		 $scope.showMe = !$scope.showMe;
		debugger;
		var registrationId=	localStorage.getItem('registrationId');
		RAService.viewcustomermsa(registrationId).then(function(data){
		      $scope.msalist=data.result;
		      debugger;
		      console.log($scope.msalist);      	
		      
		      },function(err){
		      if(err){
		          $scope.errorMessage = err; 
		     	}
		      })	
		}
	
	
	
	
	
	
	
	
	
}]);