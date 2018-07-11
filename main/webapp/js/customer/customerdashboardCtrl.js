resourceApp.controller('customerdashboardCtrl', function ($scope,$http) {
	debugger;
	
	$http.get("https://api.myjson.com/bins/aeoqi")
    .then(function(response) {
        $scope.myWelcome = response.data;
        console.log( $scope.myWelcome);
    
        $scope.mydonut($scope.myWelcome);
      
        console.log($scope.myWelcome)
    });
	
    
	    $scope.mydonut = function(object){
	    	debugger;
	        Morris.Donut({
	        element: 'donut-example',
	        data:object

	        });
	    };
	    
	    $http.get("https://api.myjson.com/bins/x3jde")
	    .then(function(response) {
	        $scope.linechart = response.data;
	        console.log( $scope.linechart);
	    
	        $scope.line($scope.linechart);
	      
	        console.log($scope.myWelcome)
	    });
		
	    
		    $scope.line = function(object){
		    	debugger;
		    	new Morris.Line({
		        element: 'myfirstchart',
		        data:object,
		        xkey: 'year',
				  
				  ykeys: ['value'],
				 
				  labels: ['Value']
		        });
		    };
	  
	  
	    
	    	
/*	new Morris.Line({
		  // ID of the element in which to draw the chart.
		  element: 'myfirstchart',
		  // Chart data records -- each entry in this array corresponds to a point on
		  // the chart.
		  
			  data: [
		     {
		    	 "year": "2013", "value": "100" },
		     
		    { 
		    		 "year": "2014", "value": "50" 
		    },
		    { year: '2015', value: 75 },
		    { year: '2016', value: 60 },
		    { year: '2017', value: 90 },
		  ]
	,
		
		  xkey: 'year',
		  
		  ykeys: ['value'],
		 
		  labels: ['Value']
	
	
		});*/
	
	/* Morris.Donut({
		        element: 'donut-example',
		         data: [
		           {label: "Ui Developers", value: 600},
		           {label: "Java Developers", value: 800},
		           {label: "Sailpoint", value: 200},
		           {label: "Oracle IDM", value: 250},
		           {label: ".Net Developer", value: 180}
		         ]
		       });*/
	
	 /*
	    Morris.Area({
	    	  element: 'area-example',
	    	  data: [
	    	    { y: '2012', a: 100, b: 90 },
	    	    { y: '2013', a: 75,  b: 65 },
	    	    { y: '2014', a: 50,  b: 40 },
	    	    { y: '2015', a: 75,  b: 65 },
	    	    { y: '2016', a: 50,  b: 40 },
	    	    { y: '2017', a: 75,  b: 65 },
	    	    { y: '2018', a: 100, b: 90 }
	    	  ],
	    	  xkey: 'y',
	    	  ykeys: ['a', 'b'],
	    	  labels: ['Requirement A', 'Resource B']
	    	});*/
	
	 Morris.Bar({
		       element: 'bar-example',
		         data: [
		          { y: '2013',a: 100, b: 90 },
		         { y: '2014', a: 50,  b: 20 },
		           { y: '2015', a: 75,  b: 40 },
		          { y: '2016', a: 60,  b: 52 },
		          { y: '2017', a: 90,  b: 75 }
		          
		         ],
		         xkey: 'y',
		        ykeys: ['a', 'b'],
		         labels: ['Requirements A', 'Resources B']
		       });
	
            });