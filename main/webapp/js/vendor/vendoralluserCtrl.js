resourceApp.controller('vendoruserlistCtrl',["$scope","$state","$stateParams","RAService",function($scope,$state,$stateParams,RAService){
	
	$scope.$on('$viewContentLoaded', function () {
	 	$scope.getUserDetails();
    });
    $scope.getUserDetails = function(){
    	$scope.local = localStorage.getItem('registrationId');	
    
    	RAService.vendoruserlist($scope.local).then(function(data){
			$scope.userList = data.result;
			console.log("vendor user list"+$scope.userList);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
    }
		
}]);

resourceApp.controller('vendoruseraddCtrl',["$scope","$state","$stateParams","RAService",function($scope,$state,$stateParams,RAService){
	
	$scope.$on('$viewContentLoaded',function(){
		$scope.alluser = {};
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
			$state.go('vendor.alluserlist');
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}else{
				$scope.errorMessage = err;
           }   
        })
	}
	
}]);

