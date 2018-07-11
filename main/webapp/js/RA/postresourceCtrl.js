resourceApp.controller('postresourceCtrl',['$scope',"$state","RAService",function($scope,$state,RAService){
	$scope.$on('$viewContentLoaded', function () {
		$scope.postresourcelist();
	})
	
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
//	
//	$scope.softlockResource = function(presource){
//		debugger;
//		if(presource.softLock == "YES"){
//			presource.softLock = "NO";
//		RAService.PostresourceSoft(presource).then(function(data){
//			$scope.Presource = data;
//			console.log($scope.Presource);
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}
//		})
//		} else {
//			presource.softLock = "YES";
//			RAService.PostresourceSoft(presource).then(function(data) {
//				$scope.Presource = data;
//				console.log($scope.Presource);
//			}, function(err) {
//				if (err) {
//					$scope.errorMessage = err;
//				}
//			})
//		}
//}
	
	
	
}]);

resourceApp.controller('postresourceByIdCtrl',['$scope','$state','$stateParams','RAService',function($scope,$state,$stateParams,RAService){
	$scope.$on('$viewContentLoaded', function(){
		$scope.resourcepostById();
		$scope.postresource = {};
	})

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
		debugger;
		$scope.idobject = id;
		$scope.name = name;
		$scope.softLock = softlock;
		$scope.hardLock=hardlock;
		if($scope.softLock == "YES"){
			$scope.root = "This Resource is already engaged with some other Customer"
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
	
	
	
}])
