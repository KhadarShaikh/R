resourceApp.controller('vendor1updateCtrl',["$scope","$state","$stateParams","RAService",function($scope, $state, $stateParams, RAService){
	$scope.$on('$viewContentLoaded',function(){
		$scope.getalluser();
		$scope.getroles();
	})
	
	$scope.getroles = function(){
		RAService.allusergetroles().then(function(data){
			$scope.alluser1 = data.result;
			console.log($scope.alluser1);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
	}
	
	$scope.userstatus = ["Active","Inactive"];
	$scope.getalluser = function(){
		RAService.allusergetbyid($stateParams.alluserId).then(function(data){
			$scope.alluser = data.result;
			console.log($scope.alluser)
			localStorage.setItem('alluserid', $scope.alluser._id);
		})
	}
	
	$scope.alluserupdate = function(){
		debugger;
		$scope.alluser._id = localStorage.getItem('alluserid');
		RAService.alluserupdate($scope.alluser).then(function(data){
			$scope.qqq = data.result;
			console.log($scope.qqq);
			$state.go('vendor.alluserlist');
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}else{
				$scope.errorMessage = err;
           }   
        })
	}
	
}])


