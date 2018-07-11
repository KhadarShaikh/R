resourceApp.controller('buynowCtrl',['$scope','RAService',function($scope,RAService){
	$scope.$on('$viewContentLoaded', function () {
		$scope.getplans();
	})
	
	$scope.getplans = function(){
		RAService.buynow().then(function(data){
			debugger;
			$scope.UserDetails = data.result;
			console.log($scope.UserDetails);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		});
	}
/*	$scope.plan=function(bid)
	{
		debugger;
		localStorage.setItem("planId",bid);
		console.log("stored in local");
		
	}*/
	}])