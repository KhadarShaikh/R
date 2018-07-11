resourceApp.controller('resourcesearch1Ctrl',['$scope','$rootScope','RAService','$state',function($scope,$rootScope,RAService,$state){
	  var registrationId = localStorage.getItem('registrationId');
	  var skills = $rootScope.skills;
	  var jobCategory = $rootScope.jobCategory;
	  var jobLocation = $rootScope.jobLocation;
	  debugger;
	  var totalExperience = $rootScope.totalExperience;
	 	RAService.searchresourcebyid(registrationId,skills,jobCategory,jobLocation,totalExperience).then(function(data){
	 		debugger;
	 		$scope.resourcelist = data.result;
	 		console.log($scope.resourcelist)	

	 	})
	 	$scope.skills = [ "Java", "jsp", "Servlets", "Spring",
							"HTML", "CSS", "Bootstrap", "AngularJs", "Nodejs",
							"Php", "Phyton", "MySQL", "MongoDB", "Oracle",
							"SQL Server" ];
		$scope.allListsfunc= function(){
			debugger;
	 		RAService.resourcelistaccordion().then(function(data){
	 	        $scope.skillslist = data.skillsMap;
	 	        console.log("sdkjfsdlkj"+$scope.skillslist);
	 	       
	 	       
	 	    });
	 		}
	 		$scope.companyfunc= function(){
	 			RAService.resourcelistaccordion().then(function(data){
	 		        $scope.company = data.vendorSet;
	 		       
	 		    });
	 			}
	 		$scope.budgetfunc= function(){
	 			RAService.resourcelistaccordion().then(function(data){
	 		        $scope.budget = data.budgetMap;
	 		       
	 		    });
	 			}
	 		$scope.experiencefunc=function(){
	 		RAService.resourcelistaccordion().then(function(data){
	 		$scope.experience =data.yearsOfExperienceMap;
	 		console.log($scope.experience)
	 		})
	 		}
	 		$scope.locationfunc = function(){
	 		RAService.resourcelistaccordion().then(function(data){
	 			$scope.location = data.locationMap;
	 		})
	 		}
	 		$scope.jobcategoryfunc = function(){
	 		RAService.resourcelistaccordion().then(function(data){
	 			$scope.Jobcategory = data.jobCategoryMap;
	 		})
	 		}
		     $scope.getResourceById = function(){
		    	 $scope.resource.registrationId = $scope.comId;
		        	RAService.resourcegetById($stateParams.resourceId).then(function(data){
							$scope.resource = data.result;
							$scope.resource.dateOfBirth = new Date(
									$scope.resource.dateOfBirth);
							console.log($scope.resource.primarySkills);
							console.log($scope.resource);
							$scope.resource.primarySkills = $scope.resource.primarySkills.split(',');
							$scope.resource.secondarySkills = $scope.resource.secondarySkills.split(',');
							 $scope.resource.preferredLocation=$scope.resource.preferredLocation.split(',');
							console.log($scope.resource.primarySkills);
							console.log($scope.resource.secondarySkills);
							}),
							function(err){
								if(err){
									$scope.errorMessage = err;
								}else{
									$scope.errorMessage = err;
							   }   
							}
		}

	 

	 
	 	 $scope.praposeResource1 = function(_id){
		 	var requirementId =   $rootScope._id;
		 	  var registrationId1 = $rootScope.registrationId1
		 	var registrationId =   localStorage.getItem('registrationId');
		 	debugger;
		 	RAService.praposeResourceCustomer(requirementId,_id,registrationId1,registrationId).then(function(data){
			 		//$scope.resourcelist = data;
			 		console.log(data.result);
			 		alert("success");
			 	})	
		 	}
	 	 
	 	 
	 	 //side filter by click checkbox
	 	 
	 	$scope.search1 = { skills: [] };
		  $scope.job = { jobCategory: [] };
		  $scope.exp = {totalExperience:[]};
		  $scope.location1 = {city:[]};
		  $scope.customer1 = {customer:[]};
		  $scope.budget1 = {budget:[]};
		$scope.search2 = function(){	
			debugger;
				  if($scope.search1.skills.length == 0){
					  $scope.search1.skills = "undefined";
				  }
				  if($scope.job.jobCategory.length == 0){
					  $scope.job.jobCategory = "undefined";
				  }
				  if($scope.exp.totalExperience.length == 0){
					  $scope.exp.totalExperience = "undefined";
				  }
				  if($scope.location1.city.length == 0){
					  $scope.location1.city = "undefined";
				  }
				  if($scope.customer1.customer.length == 0){
					  $scope.customer1.customer = "undefined";
				  }
				  if($scope.budget1.budget.length == 0){
					  $scope.budget1.budget = "undefined";
				  }
				 debugger;
	      RAService.searchsidefilterresource($scope.search1.skills,$scope.job.jobCategory,$scope.location1.city,$scope.exp.totalExperience,$scope.customer1.customer,$scope.budget1.budget).then(function(data){
		    			$scope.resourcelist = data.result;
		    			console.log($scope.resourcelist);
		    		})
		    	 
		}
	 	 
	 	 

}]);
