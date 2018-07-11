resourceApp.controller('customerCtrl',['$scope','$state','RAService',function($scope,$state,RAService){
		var user = localStorage.getItem('use');
		var admin =localStorage.getItem('admi');
		$scope.vvv = localStorage.getItem('registrationType');
		$scope.id=localStorage.getItem('registrationId');
		$scope.register =  $scope.vvv.split(',');
		$scope.user1 = localStorage.getItem('user')
		console.log($scope.user1)
		console.log($scope.register);
		$scope.model = "Customer";

		$scope.dataregister = function(){
			
			if($scope.registerData == "RA"){
				$state.go('RA.dashboard');
			}
			if($scope.registerData == "vendor"){
				$state.go('vendor.dashboard');
			}
			if($scope.registerData == "customer"){
				$state.go('customer.dashboard');
			}			  
		}
		
       if(admin== "true"){
			$scope.all_users_type=true;
		}else if(user == "true" && admin == "true"){
		    $scope.all_users_type=true;
		}
		else if(user == "true"){
			$scope.all_users_type=false;
		}
		else{

		}
       
       
   	
		$scope.PlanGetById=function(){
			debugger;
		var registrationId=	localStorage.getItem('registrationId');
			RAService.GetPlanById(registrationId).then(function(data){
		       		$scope.planlist=data.result;
		       		$scope.noOfDays=data;
		       		debugger;
		       		console.log($scope.planlist);      		
		       		
		       },function(err){
		       if(err){
		           $scope.errorMessage = err; 
		      	 }
		       })	
			}
		//In Active plans list Ctrl
		$scope.showMe = false;
		$scope.PreviousPlans=function(){
			 $scope.showMe = !$scope.showMe;
			debugger;
			var registrationId=	localStorage.getItem('registrationId');
			RAService.GetPreviousplanss(registrationId).then(function(data){
			      $scope.previousplanlist=data.result;
			      debugger;
			      console.log($scope.previousplanlist);      	
			      
			      },function(err){
			      if(err){
			          $scope.errorMessage = err; 
			     	}
			      })	
			}
	
}])