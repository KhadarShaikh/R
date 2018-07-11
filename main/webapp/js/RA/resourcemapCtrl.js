resourceApp.controller('resourcemapCtrl',["$scope","$state","$stateParams","RAService",function($scope,$state,$stateParams,RAService){
	$scope.$on('$viewContentLoaded', function () {
		$scope.companyNameList=[];
		 $scope.companyId=[];
	})
	$scope.type = ["CompanyName","RequirementType"];
	$scope.resourcelist = false;
	$scope.selecttype= '';
	$scope.resourcemaplist = function(){
		debugger;
		if($scope.selecttype == "CompanyName"){
				RAService.getCompanyList().then(function(data) {
            debugger;
			
			$scope.companyNameList=[]
             $scope.list=data.result;
			 console.log($scope.list);
			 for(var i=0; i< $scope.list.length;i++){ 
				$scope.companyNameList.push($scope.list[i].companyName);
				$scope.companyId.push($scope.list[i]._id);
			 }
				$scope.companyid = function(){
		  debugger;
		 for(var j=0;j<$scope.companyNameList.length;j++){

            if($scope.companyName1 == $scope.companyNameList[j]){
               $scope.comId = $scope.companyId[j];
			   console.log($scope.comId);
            }
		 }
	  }
        });	
		}
		else{
			RAService.postareqList().then(function(data) {
				debugger;
				$scope.companyNameList=[]
				$scope.list = data.result;
				console.log($scope.list);
				for(var k=0; k< $scope.list.length;k++){
					$scope.companyNameList.push($scope.list[k].jobTitle);
					$scope.companyId.push($scope.list[k]._id);
				}
				$scope.companyid = function(){
					debugger;
					for(var l=0;l<$scope.companyNameList.length;l++){
						if($scope.companyName1 == $scope.companyNameList[l]){
							$scope.comId = $scope.companyId[l];
							console.log($scope.comId);
						}
						
					}
				}
			});
			
		}
	}
	
	$scope.submitdata = function(){
		debugger;
		$scope.resourcelist = true;
		$scope.filter = {
			selecttype: $scope.selecttype,
			nameId: $scope.comId,
			localId: localStorage.getItem('registrationId')
				
			}
		console.log($scope.filter)
		
		RAService.mappingresource($scope.filter).then(function(data){
			$scope.resourcemap123 = data.result;
			debugger;
			console.log($scope.resourcemap123);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
	}	
}])
