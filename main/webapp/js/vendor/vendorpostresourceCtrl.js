resourceApp.controller('vendorpostresourceCtrl',['$scope','$state','RAService',function($scope,$state,RAService){
	

	$scope.$on('$viewContentLoaded', function () {
		$scope.postresourcelist();
	});
	$scope.States=["Hyderabad","Vijayawada","Vizag","Bangalore","Chennai","Madurai","Kolkata","Pune","Mumbai","Noida","Delhi","Jaipur","Darjeeling","Kerala"];

	
	
	$scope.Jobc=["Application Developer", "Applications Engineer","Database Administrator","Front End Developer","Java Developer","Junior Software Engineer","Network Engineer",
		
		"Senior Database Administrator","Senior Programmer","Senior Security Specialist","Senior Web Developer","Software Architect","Systems Designer","Software Developer",
		"Web Administrator","Web Developer"];
		$scope.skills=["java","jsp","servlets","Spring","Html","Css","Bootstrap","Angularjs","Nodejs","Php","Phyton","MySQL","MongoDB","Oracle","Sql Server"];
		$scope.experience=["0-1 years","1-2 years","2-3 years","3-4 years","4-5 years","5-6 years","more"];
		$scope.company=["TCS","Tech M","Oracle","IBM","Ojas","HCL","Wipro","Info-tech","CapGemini","Persistant","Virtusa","Infosys"];
	
	$scope.postresourcelist = function(){
		RAService.postareqList().then(function(data){
			$scope.postresource = data.result;
			$scope.localid =localStorage.getItem('registrationId');
			console.log($scope.localid);
			console.log($scope.postresource)
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
	}

	
	
	
}]);
resourceApp.controller('vendorpostresourceByIdCtrl',['$scope','$state','$stateParams','RAService',function($scope,$state,$stateParams,RAService){
	$scope.$on('$viewContentLoaded', function(){
		$scope.resourcepostById();
		$scope.postresource = {};
	});
$scope.States=["Hyderabad","Vijayawada","Vizag","Bangalore","Chennai","Madurai","Kolkata","Pune","Mumbai","Noida","Delhi","Jaipur","Darjeeling","Kerala"];

	
	
	$scope.Jobc=["Application Developer", "Applications Engineer","Database Administrator","Front End Developer","Java Developer","Junior Software Engineer","Network Engineer",
		
		"Senior Database Administrator","Senior Programmer","Senior Security Specialist","Senior Web Developer","Software Architect","Systems Designer","Software Developer",
		"Web Administrator","Web Developer"];
		$scope.skills=["java","jsp","servlets","Spring","Html","Css","Bootstrap","Angularjs","Nodejs","Php","Phyton","MySQL","MongoDB","Oracle","Sql Server"];
		$scope.experience=["0-1 years","1-2 years","2-3 years","3-4 years","4-5 years","5-6 years","more"];
		$scope.company=["TCS","Tech M","Oracle","IBM","Ojas","HCL","Wipro","Info-tech","CapGemini","Persistant","Virtusa","Infosys"];

	$scope.resourcepostById = function(){
		RAService.postresourceById($stateParams.localId,$stateParams.resourceById,$stateParams.objectid).then(function(data){
			$scope.resourceById = data.result;
			console.log($scope.resourceById);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
	}
	$scope.getById = function(id,name,softlock,hardlock){
		$scope.idobject = id;
		$scope.name = name;
		$scope.softLock = softlock;
		$scope.hardLock=hardlock;
		if($scope.hardLock == "YES"){
			$scope.root = " This Resource is already engaged with some other Customer"
		}
	}
	$scope.mappingpostresource = function(){
		debugger;
		$scope.postresource.companyId = $stateParams.resourceById;
		$scope.postresource.vendorId = $stateParams.localId;
		$scope.postresource.resourceId = $scope.idobject;
		$scope.postresource.requirementId = $stateParams.objectid;
		RAService.postresourceMapping($scope.postresource).then(function(data){
		$scope.resourcemapping = data.result;
		console.log($scope.resourcemapping)
	},function(err){
		if(err){
			$scope.errorMessage = err;
		}
	})
	}
	$scope.dynamicsearch = function(primarySkills,jobCategory,jobLocation,experience){
		debugger;
		 RAService.searchrequirement(primarySkills,jobCategory,jobLocation,experience).then(function(data){
			   $scope.requirement = data.result;
			   console.log($scope.requirement);
		   })			
	}
}])
