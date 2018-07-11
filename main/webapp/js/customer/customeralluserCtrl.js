resourceApp.controller('customeruserlistCtrl',["$scope","$state","$stateParams","RAService",function($scope,$state,$stateParams,RAService){
	
	$scope.$on('$viewContentLoaded', function () {
	 	$scope.getUserDetails();
    });
    $scope.getUserDetails = function(){    	
    	$scope.local = localStorage.getItem('registrationId');	
    	RAService.customeruserlist($scope.local).then(function(data){
			$scope.userList = data.result;
			console.log($scope.userList);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
    }
		
}]);

resourceApp.controller('customeruseraddCtrl',["$scope","$state","$stateParams","RAService",function($scope,$state,$stateParams,RAService){
	
	$scope.$on('$viewContentLoaded',function(){
	
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
	
	$scope.userall = function(){
		debugger;
		$scope.alluser.registrationId = localStorage.getItem('registrationId');
		$scope.alluser.registrationType = localStorage.getItem('registrationType');
		RAService.alluseradd($scope.alluser).then(function(data){
			$scope.alluseradd1 = data.result;
			console.log($scope.alluseradd1);
			$state.go('customer.alluserlist');
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}else{
				$scope.errorMessage = err;
           }   
        })
	}
	
	
	
	
	
}]);


resourceApp.controller('customer1updateCtrl',["$scope","$state","$stateParams","RAService",function($scope, $state, $stateParams, RAService){
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
			$state.go('customer.alluserlist');
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}else{
				$scope.errorMessage = err;
           }   
        })
	}
	
}])


