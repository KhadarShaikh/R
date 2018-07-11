resourceApp.controller('planCtrl',['$scope','RAService',function($scope,RAService){
	$scope.$on('$viewContentLoaded', function () {
		$scope.getplans();
		$scope.plan={};
	})
	//pagination
	  $scope.maxSize = 2;     // Limit number for pagination display number.  
	    $scope.totalCount = 0;  // Total number of items in all pages. initialize as a zero  
	    $scope.pageIndex = 1;   // Current page number. First page is 1.-->  
	    $scope.pageSizeSelected = 5; // Maximum number of items per page.
	    
	$scope.getplans = function(){
		RAService.buynow($scope.pageIndex,$scope.pageSizeSelected).then(function(data){
			debugger;
			$scope.UserDetails = data.result;
			console.log($scope.UserDetails);
			 $scope.totalCount = data.count;
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		});
	}
	$scope.getplans();
	 $scope.pageChanged = function() {
     $scope.getplans()
         console.log('Page changed to: ' + $scope.pageIndex);
 };

	$scope.statusplan = function(plan){
		debugger;
		if(plan.status == "Active"){
			plan.status = "InActive";
		RAService.planStatus(plan).then(function(data){
			$scope.plan = data.result;
			console.log($scope.plan);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
		} else {
			plan.status = "Active";
			RAService.planStatus(plan).then(function(data) {
				$scope.plan = data.result;
				console.log($scope.plan);
			}, function(err) {
				if (err) {
					$scope.errorMessage = err;
				}
			})
		}
}
	}]);