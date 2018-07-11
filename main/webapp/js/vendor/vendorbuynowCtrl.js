resourceApp.controller('vendorbuynewCtrl',['$scope','RAService','$stateParams','$state',function($scope,RAService,$stateParams,$state){
	$scope.$on('$viewContentLoaded',function(){
		$scope.plan();			
	})

	
		$scope.plan=function()		
		{		
			debugger;
		RAService.changeplan($stateParams.planid).then(function(data){
					debugger;
					$scope.plandetails = data.result;
					debugger;
					console.log($scope.plandetails);
					
				},function(err){
					if(err){
						$scope.errorMessage = err;
					}
				});
		}
}])

resourceApp.controller('vendorbuynowCtrl',['$scope','RAService','$stateParams','$state',function($scope,RAService,$stateParams,$state){
	$scope.$on('$viewContentLoaded',function(){
		
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
}])
	resourceApp.controller('paymentCtrl',['$scope','RAService','$stateParams','$state',function($scope,RAService,$stateParams,$state){

	$scope.validMonth=["January","February","March","April","May","June","July","August","September","October","November","December"];
	$scope.validYear=["2016","2017","2018","2019","2020","2021","2022","2023","2024"];
	$scope.cardType=["Visa","Maestro","Rupay","MasterCard"];
	
	$scope.cardDetails=function(card)	
	{
		debugger;
		$scope.card.registrationId=localStorage.getItem('registrationId');		
		RAService.paymentcarddetails(card).then(function(data){
					debugger;
					$scope.carddetails = data.result;
					debugger;
					alert("OTP sent to Your Mobile ");
					$state.go("vendor.dashboard")
					
				},function(err){
					if(err){
						$scope.errorMessage = err;
					}
				});
	}
		
	}])