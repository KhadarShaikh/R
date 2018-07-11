resourceApp.controller("homeCtrl",["$scope","$state","$stateParams","RAService",function($scope, $state, $stateParams, RAService){
	$scope.$on('$viewContentLoaded', function () {
		$scope.topVendors();
		$scope.topCustomers();
    });
    $scope.topVendors = function(){
   
    	RAService.getTopVendorslist().then(function(data){
    	
			$scope.vendorsList = data.vendorSet;
			console.log($scope.vendorsList);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
		
    }
    
    $scope.topCustomers = function(){
    	debugger;
    	RAService.getTopCustomerslist().then(function(data){
    	
			$scope.customersList = data.customerSet;
			console.log($scope.customersList);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
		
    }
    
   

}]);
    	
   
		
    
   
    	    
  


