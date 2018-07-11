resourceApp.controller('RACtrl',['$scope','$state','BlogPostService','RAService',function($scope,$state,BlogPostService,RAService){

		var user = localStorage.getItem('use');
		var admin =localStorage.getItem('admi');
	   $scope.vvv = localStorage.getItem('registrationType');
	  /* RAService.menuget(admin,$scope.vvv).then(function(data){
		   $scope.meneu = data;
	   })*/
		$scope.register =  $scope.vvv.split(',');
		console.log($scope.register);
		$scope.model = "RA";
		$scope.dataregister = function(){		
			if($scope.registerData == "RA"){
				$state.go('RA.dashboard');
			}
			if($scope.registerData == "vendor"){
				$scope.model = "vendor";
				$state.go('vendor.dashboard');
			}
			if($scope.registerData == "customer"){
				$scope.model = "customer";
				$state.go('customer.dashboard');
			}
			
		}
		

	/*	$scope.menulist = function(){
		   	debugger; 
		   	RAService.MenuListnew().then(function(data){
		       		$scope.list=data.result;
		       		console.log($scope.list);	
		       		
		       },function(err){
		       if(err){
		           $scope.errorMessage = err; 
		      	 }
		       })		       
		};*/
		
		
		$scope.PlanGetById=function(){			
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
		$scope.PreviousPlans=function(){
			debugger;
		var registrationId=	localStorage.getItem('registrationId');
			RAService.GetPreviousplan(registrationId).then(function(data){
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