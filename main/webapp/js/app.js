var resourceApp = angular.module('exampleApp', ['ui.router', 'ngCookies', 'ngResource','checklist-model', 'ngSanitize', 'ui.bootstrap','oi.select']);
	


    
    resourceApp.run(function ($rootScope, $location, $cookieStore, UserService) {

    /* Reset error when a new view is loaded */
    $rootScope.$on('$viewContentLoaded', function () {
        delete $rootScope.error;
       
    });
    $rootScope.$on('$stateChangeSuccess', function() { document.body.scrollTop = document.documentElement.scrollTop = 0; });

    $rootScope.hasRole = function (role) {

        if ($rootScope.user === undefined) {
            return false;
        }

        if ($rootScope.user.roles[role] === undefined) {
            return false;
        }

        return $rootScope.user.roles[role];
    };

//    $rootScope.logout = function () {
//        delete $rootScope.user;
//        delete $rootScope.accessToken;
//        $cookieStore.remove('accessToken');
//        $location.path("/login");
//    };

    /* Try getting valid user from cookie or go to login page */
    var originalPath = $location.path();
   //$location.path("/login");
    var accessToken = $cookieStore.get('accessToken');
    if (accessToken !== undefined) {
        $rootScope.accessToken = accessToken;
        UserService.get(function (user) {
            $rootScope.user = user;
            $location.path(originalPath);
        });
    }

    $rootScope.initialized = true;
});


resourceApp.controller('LoginController',['$scope', '$rootScope', '$state', '$cookieStore', 'UserService', 'BlogPostService','RAService',function($scope, $rootScope, $state, $cookieStore, UserService, BlogPostService,RAService){
    $scope.rememberMe = false;
 /*   $scope.$on('$viewContentLoaded', function () {
    	$scope.getplans();
    	//$scope.registration = {};
    })*/



    $scope.login = function () {
        if(!$scope.username){
            
        }else if(!$scope.password){
            
        }
        else{
              UserService.authenticate($.param({
                    username: $scope.username,
                    password: $scope.password
                }), function (authenticationResult) {
                    var accessToken = authenticationResult.result.token;
                    $scope.userName = authenticationResult.result.userName;
                    console.log($scope.username);
                    $rootScope.accessToken = accessToken;
                    if ($scope.rememberMe) {
                        $cookieStore.put('accessToken', accessToken);
                    }
                    
                    BlogPostService.user($scope.userName).then(function(response){
                        $scope.user = response.data;
                        console.log($scope.user);
                        $scope.user.registrationType  = $scope.user.result.registrationType.split(',');
                        console.log($scope.user.registrationType);
                        localStorage.setItem('registrationType', $scope.user.result.registrationType);
                        localStorage.setItem('registrationId', $scope.user.result.registrationId);
                        localStorage.setItem('use', $scope.user.result.roles.USER);
                        localStorage.setItem('admi',$scope.user.result.roles.SUPERADMIN);
                        
                      
                    // localStorage.getItem('registrationType');
                   
                          if($scope.user.registrationType.length > 1 && $scope.user.registrationType[0] == "RA"){
                            console.log("RA Module");
                             $state.go('RA.dashboard');
                          }
                          if($scope.user.registrationType.length > 1 && $scope.user.registrationType[0] == "customer"){
                             console.log("customer Module")
                             $state.go('customer.dashboard');
                          }
                         if($scope.user.registrationType.length > 1 && $scope.user.registrationType[0] == "vendor"){
                              console.log("customer Module")
                              $state.go('customer.dashboard');
                          }
                        if($scope.user.registrationType.length == 1){
                            if($scope.user.registrationType[0] == "RA"){
                            console.log("RA Module");
                            $state.go('RA.dashboard');
                        }
                        }
                        if($scope.user.registrationType.length == 1){
                            if($scope.user.registrationType[0] == "vendor"){
                            $state.go('vendor.dashboard');
                        }
                        }
                        
                        if($scope.user.registrationType.length == 1){
                            if($scope.user.registrationType[0] == "customer"){
                            console.log("customer Module")
                            $state.go('customer.dashboard');
                        }
                        } 
                        
                        
                    })
                });
        }
      
    };
    
    $scope.showMe = false;
    $scope.showus=true;
    $scope.myFunc = function() {
        $scope.showMe = !$scope.showMe;
        $scope.showus=!$scope.showus;
    };
    
    $scope.showMe = true;
    $scope.showus=false;
    $scope.myFunct = function() {
        $scope.showMe = !$scope.showMe;
        $scope.showus=!$scope.showus;
    };
	$scope.allcompanieslist = function(){
		debugger;
		RAService.companieslist().then(function(data){		
			$scope.Users = data.result;	
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		});
	}
    
    
    
	$scope.getplans = function(){
		RAService.planlist().then(function(data){
			debugger;
			$scope.UserDetails = data.result;
			
			console.log(data);
			
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		});
	}
	
	//planid
	$scope.plan=function(bid){
					debugger;
					localStorage.setItem('planid',bid);
					console.log("stored");
				}
	
	//registration
	$scope.companytype = ["Public Limited Company","Private Limited Company","Partnership","Proprietary"];
	$scope.quality = ["ISO 9001","ISO 9002","ISO I400","NONE"];
	$scope.registrationtype = ["Customer","Vendor"];
//  $scope.companieslist = [ "Ojas","Chennai","Hyderabad","Pune"];
//	$scope.customers=["Ojas","HP","Infogain","HappiestMinds"];
//	$scope.vendors=["TCS","Deloitte","Global Logic","Gspann","TechMahindra","Expansion","UST Global","Capgemini","Persistent"];
	$scope.Licences = ['1','2','3','4','5'];
	$scope.Period = ['1','2','3','4','5'];
	
	$scope.addData = function(){
		debugger;		
		$scope.registration.sowUser=$scope.registration.sowUser.toString();
		$scope.registration.registrationType = $scope.registration.registrationType.toString();
		$scope.registration.planId = localStorage.getItem('planid');
		debugger;
		RAService.saveRegistration($scope.registration).then(function(data){
			$scope.dddd = data.result;
			console.log($scope.dddd);
			$state.go('login');
			
		},function(err){    
			
			   
			if(err){
				$scope.errorMessage = err;
			}else{
				$scope.errorMessage = err;
			}
		});
	}
	
	
	
    
}])


resourceApp.controller('LoginCtrl',["$scope","$rootScope","$location",function($scope,$rootScope,$location){
        $scope.logout = function(){
            localStorage.clear();
            $location.path('/');
        }
}])

resourceApp.factory('UserService', function ($resource) {

    return $resource('rest/user/:action', {},
        {
            authenticate: {
                method: 'POST',
                params: {'action': 'authenticate'},
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }
        }
    
    );
});

resourceApp.factory('BlogPostService', function ($resource,$http) {

//    return $resource('rest/blogposts/:id', {id: '@id'});
    var obj = {};
    obj.user = function(userName){
        return $http.get('rest/user/userDetails/' +userName);
    }
    return obj;
});

//plans

resourceApp.controller('buynowCtrl',['$scope','RAService',function($scope,RAService){
	$scope.$on('$viewContentLoaded', function () {
		$scope.getplans();
	})
	
	$scope.getplans = function(){
		RAService.buynow().then(function(data){
			debugger;
			$scope.UserDetails = data.result;
			console.log($scope.UserDetails);
		},function(err){
			if(err){
				$scope.errorMessage = err;
			}
		});
	}

}])
