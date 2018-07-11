resourceApp.controller('RAlistCtrl',['$scope','$document','RAService',function($scope,$document,RAService){
	$scope.$on('$viewContentLoaded', function () {
		//$scope.registration = {};
		$scope.registrationlist();
	})
	$scope.title = "Registration";
	$scope.status = "active";
	//pagination
	  $scope.maxSize = 2;     // Limit number for pagination display number.  
	    $scope.totalCount = 0;  // Total number of items in all pages. initialize as a zero  
	    $scope.pageIndex = 1;   // Current page number. First page is 1.-->  
	    $scope.pageSizeSelected = 5; // Maximum number of items per page.
	    
	
	$scope.registrationlist = function(){
		RAService.RAReg($scope.pageIndex,$scope.pageSizeSelected).then(function(data){
			$scope.details = data.result;
			console.log($scope.details);
			 $scope.totalCount = data.count;
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		})
	}
	$scope.registrationlist();
	 $scope.pageChanged = function() {
      $scope.registrationlist()
          console.log('Page changed to: ' + $scope.pageIndex);
  };
	
	$scope.statusId = function(registration){
		debugger;
		if(registration.status == "Active"){
			registration.status = "InActive";
		RAService.registrationStatus(registration).then(function(data){
			$scope.ssss = data.result;
			console.log($scope.ssss);
			},function(err){
				if(err){
					$scope.errorMessage = err;
				}
			})
		}else{
				registration.status = "Active";
		RAService.registrationStatus(registration).then(function(data){
			$scope.ssss = data.result;
			console.log($scope.ssss);
			},function(err){
				if(err){
					$scope.errorMessage = err;
				}
			})
		}
		
		
	}
//	$scope.RegisterDeleteById = function(registration){
//		RAService.deleteregistration(registration).then(function(data){
//			$scope.deleteMesssage = data;
//			console.log("Delete Successfully..!!");
//			$scope.registrationlist();
//		})
//	}

}]);

resourceApp.controller('addRACtrl',['$scope','$state','RAService',function($scope,$state,RAService){
	$scope.$on('$viewContentLoaded', function () {
		$scope.registration = {};
	})
	$scope.companytype = ["Public Limited Company","Private Limited Company","Partnership","Proprietary"];
	$scope.quality = ["ISO 9001","ISO 9002","ISO I400","NONE"];
	$scope.registrationtype = ["RA","Customer","Vendor"];
	
	$scope.States=["Andaman and Nicobar Islands","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chandigarh","Chhattisgarh","Delhi","Goa","Gujarat","Haryana",
		"Himachal Pradesh","Jammu and Kashmir","Jharkhand","Karnataka","Kerala","Lakshadweep","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland",
		"Orissa","Pondicherry","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttaranchal","Uttar Pradesh","West Bengal"];
	
	$scope.Countries = ["Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", 
		"Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil",
		"British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", 
		"Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", 
		"Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
		"East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
		"Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia",
		"Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", 
		"Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia",
		"Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", 
		"Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands",
		"Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines",
		"Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", 
		"St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga",
		"Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
		"United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela",
		"Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"]

	$scope.addData = function(){
		debugger;
		$scope.registration.registrationType = $scope.registration.registrationType.toString();
		$scope.registration.planId=localStorage.getItem("planId");
		RAService.saveRegistration($scope.registration).then(function(data){
			$scope.dddd = data.result;
			console.log($scope.dddd);
			$state.go('RA.RAlist');
		},function(err){    
			
			   
			if(err){
				$scope.errorMessage = err;
			}else{
				$scope.errorMessage = err;
			}
		});
	}
}]);
