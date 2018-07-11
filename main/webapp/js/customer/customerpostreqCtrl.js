resourceApp.controller('customerpostareqCtrl',['$scope','RAService','$rootScope','$state',function($scope,RAService,$rootScope,$state){    
	/*$scope.$on('$viewContentLoaded', function () {
		$scope.postareq();		
	})*/
	
	$scope.allListsfunc= function(){
 		RAService.requirementlistaccordion().then(function(data){
 		   $scope.skillslist = data;
 	        console.log($scope.list);
 	       
 	    });
 		}
		
	//pagination
	  $scope.maxSize = 2;     // Limit number for pagination display number.  
	    $scope.totalCount = 0;  // Total number of items in all pages. initialize as a zero  
	    $scope.pageIndex = 1;   // Current page number. First page is 1.-->  
	    $scope.pageSizeSelected = 3; // Maximum number of items per page.	
	$scope.postareq = function(){
		$scope.local = localStorage.getItem('registrationId');
		RAService.customeraddrequirment($scope.local,$scope.pageIndex,$scope.pageSizeSelected).then(function(data) {
			debugger;
	        $scope.list = data.result;
	        $scope.skillslist= data.allList;
	        console.log($scope.list);
	        $scope.totalCount = data.count;
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
		
	}
	$scope.pageChanged = function() {
        $scope.postareq()
            console.log('Page changed to: ' + $scope.pageIndex);
    };
	
	$scope.requirement = function(postrequirement){
		debugger;
		if(postrequirement.status == "Active"){
			postrequirement.status = "Inactive";
		RAService.requirementStatus(postrequirement).then(function(data){
			$scope.aaaa = data.result;
			console.log($scope.aaaa);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
		}else{
			postrequirement.status = "Active";
		RAService.requirementStatus(postrequirement).then(function(data){
			$scope.aaaa = data.result;
			console.log($scope.aaaa);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
		}	
	}
	$scope.searchresource = function(_id,primarySkills,jobCategory,jobLocation,experience,registrationId) {
		debugger;
		
		$rootScope._id=_id;
		$rootScope.experience=experience;
 		$rootScope.primarySkills1=primarySkills;
 		$rootScope.jobCategory=jobCategory;
 		$rootScope.jobLocation=jobLocation;
 		$rootScope.registrationId2=registrationId;

//		localStorage.setItem('requirementId', id);
//		localStorage.setItem('primarySkills1', primarySkills);
//		localStorage.setItem('jobCategory1', jobCategory);
//		localStorage.setItem('jobLocation1', jobLocation);
//		localStorage.setItem('experience1', experience);
	$state.go('customer.resourcesearch');
	}

/*	
	$scope.dynamicsearch = function(primarySkills,jobCategory,jobLocation,experience){
		debugger;
		 RAService.searchrequirementbyid(primarySkills,jobCategory,jobLocation,experience).then(function(data){
			 debugger;
			   $scope.list = data;
			   console.log("this is list");
			   console.log($scope.list);
		   })			
	}*/
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
	 $scope.dynamicsearch = function(primarySkills,jobCategory,jobLocation,experience){
	
			 RAService.searchrequirement(primarySkills,jobCategory,jobLocation,experience).then(function(data){
				   $scope.list = data.result;
				   console.log($scope.list);
			   })			
		}
	 $scope.search1 = function(search1){
	
			RAService.searchrequirement(search1.skills,search1.jobCategory,search1.city,search1.totalExperience).then(function(data){
				
				$scope.list = data.result;
				console.log($scope.list);
			})
		}
	 
}])