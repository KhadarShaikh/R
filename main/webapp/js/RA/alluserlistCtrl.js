resourceApp.controller("alluserlistCtrl",["$scope","$state","$stateParams","RAService",function($scope, $state, $stateParams, RAService){
	$scope.$on('$viewContentLoaded', function () {
	 	$scope.getUserDetails();
    });
    $scope.getUserDetails = function(){
    	RAService.userlist().then(function(data){
			$scope.userList = data.result;
			console.log($scope.userList);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
    }
}]);


resourceApp.controller('alluseraddCtrl',["$scope","$state","$stateParams","RAService",function($scope,$state,$stateParams,RAService){
	$scope.$on('$viewContentLoaded',function(){	
		$scope.getroles();
	})
	
	$scope.getroles = function(){
		RAService.allusergetroles().then(function(data){
			$scope.alluser1 = data.result;
			console.log($scope.alluser1);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
	}
	$scope.userstatus = ["Enable","Disable"];

	$scope.userall = function(){
		debugger;
		$scope.alluser.registrationId = localStorage.getItem('registrationId');
		$scope.alluser.registrationType = localStorage.getItem('registrationType');
		RAService.alluseradd($scope.alluser).then(function(data){
			$scope.alluseradd1 = data.result;
			console.log($scope.alluseradd1);
			$state.go('RA.alluserlist');
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}else{
				$scope.errorMessage = err;
           }   
        })
	}
}])


resourceApp.controller('alluserupdateCtrl',["$scope","$state","$stateParams","RAService",function($scope, $state, $stateParams, RAService){
	$scope.$on('$viewContentLoaded',function(){
		$scope.getalluser();
		$scope.getroles();
	})
	
	$scope.getroles = function(){
		RAService.allusergetroles().then(function(data){
			$scope.alluser1 = data.result;
			console.log($scope.alluser1);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
	}
	
	$scope.userstatus = ["Active","Inactive"];
	$scope.getalluser = function(){
		RAService.allusergetbyid($stateParams.alluserId).then(function(data){
			$scope.alluser = data.result;
			console.log($scope.alluser)
			localStorage.setItem('alluserid', $scope.alluser._id);
		})
	}
	
	$scope.alluserupdate = function(){
		debugger;
		$scope.alluser._id = localStorage.getItem('alluserid');
		RAService.alluserupdate($scope.alluser).then(function(data){
			$scope.qqq = data.result;
			console.log($scope.qqq);
			$state.go('RA.alluserlist');
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}else{
				$scope.errorMessage = err;
           }   
        })
	}
	
}])


//resourceApp.controller("alluserlistCtrl",["$scope","$state","$stateParams","RAService",function($scope, $state, $stateParams, RAService){
//	$scope.$on('$viewContentLoaded', function () {
//	 	$scope.getUserDetails();
//	 	$scope.resourcelistById();
//	 	$scope.companyId=[];
//    });
//	$scope.type = ["CompanyName","RequirementType"];
//	$scope.resourcelist = false;
//	$scope.selecttype= '';
//    $scope.getUserDetails = function(){
//    	RAService.userlist().then(function(data){
//			$scope.userList = data.result;
//			console.log($scope.userList);
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}
//		})
//    }
//    
//	$scope.resourcelistById = function(){
//	
//				RAService.getCompanyList().then(function(data) { 		
//			$scope.companyNameList=[]
//             $scope.list=data.result;
//			 console.log($scope.list);
//			 for(var i=0; i< $scope.list.length;i++){ 
//				$scope.companyNameList.push($scope.list[i].companyName);
//				$scope.companyId.push($scope.list[i]._id);
//			 }
//		
//				$scope.companyid = function(){
//					
//		 for(var j=0;j<$scope.companyNameList.length;j++){
//
//            if($scope.companyName1 == $scope.companyNameList[j]){
//               $scope.comId = $scope.companyId[j];
//			   console.log($scope.comId);
//            }
//		 }
//	  }
//        });			
//	
//	}
//	
//	$scope.submitdata = function(){
//		
//		$scope.filter=$scope.comId;
//			
//		console.log($scope.filter)
//		
//		RAService.getallusersByCompanyId($scope.filter).then(function(data){
//			$scope.userList = data.result;
//			
//			console.log($scope.userList);
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}
//		})
//	}
//    
//}]);


//resourceApp.controller('alluseraddCtrl',["$scope","$state","$stateParams","RAService",function($scope,$state,$stateParams,RAService){
//	$scope.$on('$viewContentLoaded',function(){
//		$scope.alluser = {};
//		$scope.getroles();
//	})
//	
//	$scope.getroles = function(){
//		RAService.allusergetroles().then(function(data){
//			$scope.alluser1 = data.result;
//			console.log($scope.alluser1);
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}
//		})
//	}
//	$scope.userstatus = ["Enable","Disable"];
//
//	$scope.userall = function(){
//		
//		$scope.alluser.registrationId = localStorage.getItem('registrationId');
//		$scope.alluser.registrationType = localStorage.getItem('registrationType');
//		RAService.alluseradd($scope.alluser).then(function(data){
//			$scope.alluseradd1 = data.result;
//			console.log($scope.alluseradd1);
//			$state.go('RA.alluserlist');
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}else{
//				$scope.errorMessage = err;
//           }   
//        })
//	}
//}])


//resourceApp.controller('alluserupdateCtrl',["$scope","$state","$stateParams","RAService",function($scope, $state, $stateParams, RAService){
//	$scope.$on('$viewContentLoaded',function(){
//		$scope.getalluser();
//		$scope.getroles();
//	})
//	
//	$scope.getroles = function(){
//		RAService.allusergetroles().then(function(data){
//			$scope.alluser1 = data.result;
//			console.log($scope.alluser1);
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}
//		})
//	}
//	
//	$scope.userstatus = ["Enable","Disable"];
//	$scope.getalluser = function(){
//		RAService.allusergetbyid($stateParams.alluserId).then(function(data){
//			$scope.alluser = data.result;
//			console.log($scope.alluser)
//			localStorage.setItem('alluserid', $scope.alluser._id);
//		})
//	}
//	
//	$scope.alluserupdate = function(){
//		
//		$scope.alluser._id = localStorage.getItem('alluserid');
//		RAService.alluserupdate($scope.alluser).then(function(data){
//			$scope.qqq = data.result;
//			console.log($scope.qqq);
//			$state.go('vendor.alluserlist');
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}else{
//				$scope.errorMessage = err;
//           }   
//        })
//	}
//	
//}])
//
//
//resourceApp.controller("alluserlistCtrl",["$scope","$state","$stateParams","RAService",function($scope, $state, $stateParams, RAService){
//	$scope.$on('$viewContentLoaded', function () {
//	 	$scope.getUserDetails();
//	 	$scope.resourcelistById();
//	 	$scope.companyId=[];
//    });
//	$scope.type = ["CompanyName","RequirementType"];
//	$scope.resourcelist = false;
//	$scope.selecttype= '';
//    $scope.getUserDetails = function(){
//    	RAService.userlist().then(function(data){
//			$scope.userList = data.result;
//			console.log($scope.userList);
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}
//		})
//    }
//    
//	$scope.resourcelistById = function(){
//	
//				RAService.getCompanyList().then(function(data) { 		
//			$scope.companyNameList=[]
//             $scope.list=data.result;
//			 console.log($scope.list);
//			 for(var i=0; i< $scope.list.length;i++){ 
//				$scope.companyNameList.push($scope.list[i].companyName);
//				$scope.companyId.push($scope.list[i]._id);
//			 }
//		
//				$scope.companyid = function(){
//					
//		 for(var j=0;j<$scope.companyNameList.length;j++){
//
//            if($scope.companyName1 == $scope.companyNameList[j]){
//               $scope.comId = $scope.companyId[j];
//			   console.log($scope.comId);
//            }
//		 }
//	  }
//        });			
//	
//	}
//	
//	$scope.submitdata = function(){
//		
//		$scope.filter=$scope.comId;
//			
//		console.log($scope.filter)
//		
//		RAService.getallusersByCompanyId($scope.filter).then(function(data){
//			$scope.userList = data.result;
//			
//			console.log($scope.userList);
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}
//		})
//	}
//    
//}]);
//
//
//resourceApp.controller('alluseraddCtrl',["$scope","$state","$stateParams","RAService",function($scope,$state,$stateParams,RAService){
//	$scope.$on('$viewContentLoaded',function(){
//		$scope.alluser = {};
//		$scope.getroles();
//	})
//	
//	$scope.getroles = function(){
//		RAService.allusergetroles().then(function(data){
//			$scope.alluser1 = data.result;
//			console.log($scope.alluser1);
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}
//		})
//	}
//	$scope.userstatus = ["Enable","Disable"];
//
//	$scope.userall = function(){
//		
//		$scope.alluser.registrationId = localStorage.getItem('registrationId');
//		$scope.alluser.registrationType = localStorage.getItem('registrationType');
//		RAService.alluseradd($scope.alluser).then(function(data){
//			$scope.alluseradd1 = data.result;
//			console.log($scope.alluseradd1);
//			$state.go('RA.alluserlist');
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}else{
//				$scope.errorMessage = err;
//           }   
//        })
//	}
//}])
//
//
//resourceApp.controller('alluserupdateCtrl',["$scope","$state","$stateParams","RAService",function($scope, $state, $stateParams, RAService){
//	$scope.$on('$viewContentLoaded',function(){
//		$scope.getalluser();
//		$scope.getroles();
//	})
//	
//	$scope.getroles = function(){
//		RAService.allusergetroles().then(function(data){
//			$scope.alluser1 = data.result;
//			console.log($scope.alluser1);
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}
//		})
//	}
//	
//	$scope.userstatus = ["Active","InActive"];
//	$scope.getalluser = function(){
//		RAService.allusergetbyid($stateParams.alluserId).then(function(data){
//			$scope.alluser = data.result;
//			console.log($scope.alluser)
//			localStorage.setItem('alluserid', $scope.alluser._id);
//		})
//	}
//	
//	$scope.alluserupdate = function(){
//		
//		$scope.alluser._id = localStorage.getItem('alluserid');
//		RAService.alluserupdate($scope.alluser).then(function(data){
//			$scope.qqq = data.result;
//			console.log($scope.qqq);
//			$state.go('RA.alluserlist');
//		},function(err){
//			if(err){
//				$scope.errorMessage = err;
//			}else{
//				$scope.errorMessage = err;
//           }   
//        })
//	}
//	
//}])


