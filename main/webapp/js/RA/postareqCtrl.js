resourceApp.controller('postareqCtrl',['$scope','RAService',function($scope,RAService){
	$scope.Selectors =["jobCategory","experience","jobLocation"];
    $scope.SelectedCriteria = ""; 
    $scope.filterValue = "";
    
    
	$scope.$on('$viewContentLoaded', function () {
		$scope.postrequirement = {};
		$scope.postareq();
	})
	

	
	
	
	$scope.requirement = function(postrequirement){
		
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
	
	
	
	$scope.Selectors = [ "jobCategory", "experience",
							"jobLocation" ];
					$scope.SelectedCriteria = "";
					$scope.filterValue = "";

					$scope.$on('$viewContentLoaded', function() {
						$scope.postrequirement = {};
						$scope.postareq();
					})
					$scope.skillfunc= function(){
						RAService.getskills().then(function(data){
					        $scope.skills = data.result;
					       
					    });
						}
						$scope.companyfunc= function(){
							RAService.getcompany().then(function(data){
						        $scope.company = data.result;
						       
						    });
							}
						$scope.budgetfunc= function(){
							RAService.getbudget().then(function(data){
						        $scope.budget = data.result;
						       
						    });
							}
						$scope.experiencefunc=function(){
						RAService.getexperience().then(function(data){
						$scope.experience =data.result;
						console.log($scope.experience);
						})
						}
						$scope.locationfunc = function(){
						RAService.getlocation().then(function(data){
							$scope.location = data.result;
						})
						}
						$scope.jobcategoryfunc = function(){
						RAService.getjobCategory().then(function(data){
							$scope.Jobc = data.result;
						})
						}
						$scope.dynamicsearch = function(primarySkills,jobCategory,jobLocation,experience){
							debugger;
							 RAService.searchrequirement(primarySkills,jobCategory,jobLocation,experience).then(function(data){
								   $scope.list = data.result;
								   console.log($scope.list);
							   })			
						}
				
						$scope.skills = [ "java", "jsp", "servlets", "Spring",
							"Html", "Css", "Bootstrap", "Angularjs", "Nodejs",
							"Php", "Phyton", "MySQL", "MongoDB", "Oracle",
							"Sql Server" ];
						
						$scope.maxSize = 2;     // Limit number for pagination display number.  
					    $scope.totalCount = 0;  // Total number of items in all pages. initialize as a zero  
					    $scope.pageIndex = 1;   // Current page number. First page is 1.-->  
					    $scope.pageSizeSelected = 3; // Maximum number of items per page.	
						
					$scope.postareq = function() {
						debugger;
						$scope.rid=localStorage.getItem('registrationId');
						RAService.postareqList($scope.pageIndex,$scope.pageSizeSelected).then(function(data) {
							$scope.list = data.result;
							console.log($scope.list);
							$scope.totalCount = data.count;
						}, function(err) {
							if (err) {
								$scope.errorMessage = err;
							}
						})
					}
					$scope.pageChanged = function() {
				        $scope.postareq()
				            console.log('Page changed to: ' + $scope.pageIndex);
				    };
					

					$scope.resourcesearch = function(_id,skills,jobCategory,jobLocation,totalExperience) {
						debugger;
						localStorage.setItem('requirementId', _id);
						localStorage.setItem('skills', skills);
						localStorage.setItem('jobCategory', jobCategory);
						localStorage.setItem('jobLocation', jobLocation);
						localStorage.setItem('totalExperience', totalExperience);
						$state.go('vendor.resourcecategory');
					}
					
					
					$scope.search1 = function(search1){
						debugger;
						RAService.searchrequirement(search1.skills,search1.jobCategory,search1.city,search1.totalExperience).then(function(data){
							
							$scope.list = data.result;
							console.log($scope.list);
						})
					}

					$scope.requirement = function(postrequirement) {

						if (postrequirement.status == "Active") {
							postrequirement.status = "Inactive";
							RAService.requirementStatus(postrequirement).then(
									function(data) {
										$scope.aaaa = data.result;
										console.log($scope.aaaa);
									}, function(err) {
										if (err) {
											$scope.errorMessage = err;
										}
									})
						} else {
							postrequirement.status = "Active";
							RAService.requirementStatus(postrequirement).then(
									function(data) {
										$scope.aaaa = data.result;
										console.log($scope.aaaa);
									}, function(err) {
										if (err) {
											$scope.errorMessage = err;
										}
									})
						}
					}

				
	
}])