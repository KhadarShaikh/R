resourceApp.controller('customerrequirementsearachCtrl',['$scope','RAService','$rootScope',"$stateParams",'$state',function($scope,RAService,$rootScope,$stateParams,$state){
	  $scope.$on('$viewContentLoaded', function () {
			
			 $scope.edit();
	        })
	        
	var registrationId = localStorage.getItem('registrationId');
	  var skills = $rootScope.primarySkills;
	  var jobCategory = $rootScope.jobCategory;
	  var currentLocation =$rootScope.currentLocation;
	  var yearsOfExperience = $rootScope.yearsOfExperience;
	  debugger;
	 	RAService.searchrequirementbyid(registrationId,skills,jobCategory,currentLocation,yearsOfExperience).then(function(data){
	 		$scope.list = data.result;
	 		console.log($scope.list);	
	 	})	 
	 	$scope.skills=["Java","jsp","Servlets","Spring","HTML","CSS","Bootstrap","Angularjs","Nodejs","Php","Phyton","MySQL","MongoDB","Oracle","SQL Server"];

	 	$scope.allListsfunc= function(){
			debugger;
	 		RAService.allreqLists().then(function(data){
	 	        $scope.skillslist = data;

	 	    });
	 		}
	 		
		
	 	 $scope.skil = {
	 			skills : []
	 		};
	 		$scope.job = {
	 			jobCategory1 : []
	 		};
	 		$scope.exp2 = {
	 			totalExperience : []
	 		};
	 		$scope.location2 = {
	 			city : []
	 		};
	 		$scope.customer1 = {
	 			customer : []
	 		};
	 		$scope.budget1 = {
	 			budget : []
	 		};
	 		
	 	$scope.checkbox = function() {
	 		debugger;
			if ($scope.skil.skills.length == 0) {
				$scope.skil.skills = "undefined";
			}
			if ($scope.job.jobCategory1.length == 0) {
				$scope.job.jobCategory1 = "undefined";
			}
			if ($scope.exp2.totalExperience.length == 0) {
				$scope.exp2.totalExperience = "undefined";
			}
			if ($scope.location2.city.length == 0) {
				$scope.location2.city = "undefined";
			}
			if ($scope.customer1.customer.length == 0) {
				$scope.customer1.customer = "undefined";
			}
			if ($scope.budget1.budget.length == 0) {
				$scope.budget1.budget = "undefined";
			}
			debugger;
			RAService.searchsidefilterrequirment(
					$scope.skil.skills,
					$scope.job.jobCategory1,
					$scope.location2.city,
					$scope.exp2.totalExperience,
					$scope.customer1.customer,
					$scope.budget1.budget).then(
					function(data) {
						$scope.list = data.result;
						console.log($scope.list);
					})

		}
	 	
	        $scope.edit = function(){
	        	RAService.postareqGetById($stateParams.postId).then(function(data){
						$scope.postrequirement = data.result;
						console.log($scope.postrequirement.primarySkills);
						console.log($scope.postrequirement);
						$scope.postrequirement.primarySkills = $scope.postrequirement.primarySkills.split(',');
						$scope.postrequirement.secondarySkills = $scope.postrequirement.secondarySkills.split(',');
						console.log($scope.postrequirement.primarySkills);
						console.log($scope.postrequirement.secondarySkills);
						}),
						function(err){
							if(err){
								$scope.errorMessage = err;
							}else{
								$scope.errorMessage = err;
						   }   
						}
	}
}]);


