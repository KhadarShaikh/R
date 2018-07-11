resourceApp.controller('customerupdatepostreqCtrl',["$scope","$rootScope","$state","$stateParams","RAService",function($scope,$rootScope,$state,$stateParams,RAService){
    $scope.$on('$viewContentLoaded', function () {
		$scope.postrequirement = {};       
		 $scope.companyNameList=[];
		 /*$scope.companyId=[];*/		
		 $scope.edit();
        })
		
         $scope.stage = "";
    
    // Navigation functions
    $scope.next = function (stage) {
      if ($scope.frm.$valid) {
        $scope.direction = 1;
        $scope.stage = stage;
      
      }
    };

    $scope.back = function (stage) {
      $scope.direction = 0;
      $scope.stage = stage;
    };
    
		/*$scope.companyid = function(){
			debugger;
				RAService.getCompanyList().then(function(data) {
            
             $scope.list=data.result;
			 console.log($scope.list[0].companyName);
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
			}*/
		
      
        $scope.jobcategory = ["Java Developer","UI Developer","IDM Consultant","Testing"];
	    $scope.jobtype = ["Contract","Full-time","Part-time"];
	    $scope.jobRole = ["Fresher","Intern","Trainee","Junior Developer","Senior Developer","Project Lead"];
	    $scope.joblocation= ["Bangalore","Chennai","Hyderabad","Pune","Itanagar","Dispur","Patna","Raipur","Panaji","Gandhinagar","Punjab","Shimla","Srinagar","Ranchi",
        	"Thiruvananthapuram","Bhopal","Mumbai","Imphal","Shillong","Aizawl","Kohima","Bhubaneswar","Jaipur","Gangtok","Noida","Amaravathi","Agartala","Lucknow","Dehradun","Kolkata"];
        $scope.experience = ["1-2 years","2-3 years","3-5 years","5-7 years","7-10 years"];
		$scope.Rate=["Hourly","Per-Day","Per-Week","Per-Month",];
		$scope.skills=["java","jsp","servlets","Spring","Html","Css","Bootstrap","Angularjs","Nodejs","Php","Phyton","MySQL","MongoDB","Oracle","Sql Server"];
		$scope.primaryskills = ["Big-Data","Hadoop","RPA","Java","JDBC","HTML","HTML5","CSS3","Javascript","JQuery",
			"Angular JS","Angular5","MySQL","MongoDB","POSTGRESQL","QA Testing","Manual Testing","Selenium",
			"C","C++","Core Java","IDM","Python","PHP","Salesforce","Data Science","Dot Net","C#.Net",
			"ASP.NET","DevOps","Block Chain","IOT",	"Android","iOS","Mulesoft","AWS"];
		$scope.secondaryskills = ["Machine Learning","Cyber Security","Ruby On Rails","Bootstrap","Docker Training","OpenStack","Google Cloud","Microsoft Azure","TensorFlow","EMC-SAN","NetApp","VMWare","VMWare NSX","Shell Scripting","RHEL Cluster","IBM AIX","Power HACMP","Unity","Red Hat","CCNA","CCNP","CCIE","MCSE","Exchange Server 2013","Microsoft Private Cloud","Microsoft Azure","Microsoft Office 365","Windows Server 2016","Advanced Linux","VMware vSphere","CCNA Security","Cisco ASA Firewall","Checkpoint Firewall","Hibernate","Advanced Java","Spring","Oracle","D2k","Data Structures","AJAX","JSON","SEO","OOPS","Photoshop","SPARK","TABLLEAU","QLIK VIEW","BITCOIN","Digital Marketing","Business Intelligence","SAS BI"];
        $scope.joining = ["Immediate","10-15 days","15-30 days","30-45 days","sf"];
        $scope.yearsOfExperience = ["0 year","1 years", "2 years", "3 years","4 years", "5 years", "6 years","7 years", "8 years", "9 years","10 years","11 years"];
        $scope.monthsOfExperience = ["0 month","1 month", "2 months", "3 months","4 months", "5 months", " 6 months","7 months", "8 months", "9 months","10 months","11 months"];
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

$scope.saveDetails = function () {
	debugger;
	//$scope.postrequirement.registrationId = $scope.comId;
	$scope.postrequirement.primarySkills = $scope.postrequirement.primarySkills.toString();
	$scope.postrequirement.secondarySkills = $scope.postrequirement.secondarySkills.toString();
	 $scope.postrequirement.registrationId = localStorage.getItem('registrationId');
    RAService.updatepostareq($scope.postrequirement).then(function(data){
	$scope.postdata = data.result;
	console.log($scope.postdata);
	$state.go('customer.postrequirement');
	}),function(err){
			if(err){
				$scope.errorMessage = err;
			}else{
				$scope.errorMessage = err;
           }   
        }
};  


}]);
