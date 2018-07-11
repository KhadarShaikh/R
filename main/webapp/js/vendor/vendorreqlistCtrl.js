resourceApp.controller('vendorreqlistCtrl',	['$scope','RAService','$rootScope','$state',function($scope, RAService,$rootScope, $state) {
					
					$scope.$on('$viewContentLoaded', function() {
						//$scope.allListsfunc();
						$scope.postareq();
					})
					$scope.allListsfunc= function(){
						debugger;
						RAService.allreqLists().then(function(data){
						$scope.skillslist = data;						
						console.log($scope.allListsfunc);

						});
					}						  
						$scope.dynamicsearch = function(primarySkills,jobCategory,jobLocation,experience){
							debugger;
							 RAService.searchrequirement(primarySkills,jobCategory,jobLocation,experience).then(function(data){
								   $scope.list = data.result;
								   console.log($scope.list);
							   })			
						}
					
						$scope.skills = [ "Java", "jsp", "Servlets", "Spring",
							"HTML", "CSS", "Bootstrap", "AngularJs", "Nodejs",
							"Php", "Phyton", "MySQL", "MongoDB", "Oracle",
							"SQL Server" ];
						
						
						//pagination
						  $scope.maxSize = 2;     // Limit number for pagination display number.  
						    $scope.totalCount = 0;  // Total number of items in all pages. initialize as a zero  
						    $scope.pageIndex = 1;   // Current page number. First page is 1.-->  
						    $scope.pageSizeSelected = 2; // Maximum number of items per page.
						    
						
						
						$scope.postareq = function() {
						debugger;
						
						RAService.venpostareqList($scope.pageIndex,$scope.pageSizeSelected).then(function(data) {
							$scope.list = data.result;
							$scope.skillslist = data.allList;		
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
					
			      	   $scope.skil1 = {
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
			      				if ($scope.skil1.skills.length == 0) {
			      					$scope.skil1.skills = "undefined";
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
			      				RAService.searchsidefilterrequirmentvendor(
			      						$scope.skil1.skills,
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
			             

					$scope.resourcesearch = function(skills,jobCategory,jobLocation,totalExperience) {
						debugger;
//						  var registrationId = localStorage.getItem('registrationId');
//						RAService.searchresourcebyid(registrationId,skills,jobCategory,jobLocation,totalExperience).then(function(data){
//					 		debugger;
//					 		$scope.resourcelist = data.result;
//					 		console.log($scope.resourcelist)	
//
//					 	})
						$rootScope.totalExperience=totalExperience;
				 		$rootScope.skills=skills;
				 		$rootScope.jobCategory=jobCategory;
				 		$rootScope.jobLocation=jobLocation;
				 		
//						localStorage.setItem('requirementId', _id);
//						localStorage.setItem('skills', skills);
//						localStorage.setItem('jobCategory', jobCategory);
//						localStorage.setItem('jobLocation', jobLocation);
//						localStorage.setItem('totalExperience', totalExperience);
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

				} ])
				
resourceApp.controller('vendorreqsummaryCtrl',["$scope","$rootScope","$state","$stateParams","RAService",function($scope,$rootScope,$state,$stateParams,RAService){
    $scope.$on('$viewContentLoaded', function () {
		$scope.postrequirement = {};
       
		 $scope.edit();
        })
		
   
	
		
        $scope.jobcategory = ["Java Developer","UI Developer","IDM Consultant",".Net Developer"];
	    $scope.jobtype = ["Contract","Full-time","Part-time"];
	    $scope.jobRole = ["Fresher","Intern","Trainee","Junior Developer","Senior Developer","Project Lead"];
	    $scope.joblocation= ["Bangalore","Chennai","Hyderabad","Pune","Itanagar","Dispur","Patna","Raipur","Panaji","Gandhinagar","Punjab","Shimla","Srinagar","Ranchi",
        	"Thiruvananthapuram","Bhopal","Mumbai","Imphal","Shillong","Aizawl","Kohima","Bhubaneswar","Jaipur","Gangtok","Noida","Amaravathi","Agartala","Lucknow","Dehradun","Kolkata"];
        $scope.experience = ["1-2 years","2-3 years","3-5 years","5-7 years","7-10 years"];
		$scope.primaryskills = ["Java","JDBC","HTML5","CSS3","Javascript","AngularJS"];
		$scope.Rate=["Hourly","Per-Day","Per-Week","Per-Month",];
		$scope.skills=["Java","jsp","servlets","Spring","HTML","CSS","Bootstrap","AngularJs","Nodejs","Php","Phyton","MySQL","MongoDB","Oracle","SQL Server"];
		$scope.secondaryskills = ["Oracle","MYSQL","SQL Server","MongoDB","WebRTC","Web Socket"];
        $scope.joining = ["Immediate","10-15 days","15-30 days","30-45 days"];
       
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

