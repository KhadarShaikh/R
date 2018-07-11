resourceApp.directive('fileDownload',function(){
    return{
        restrict:'A',
        scope:{
            fileDownload:'=',
            fileName:'=',
        },

        link:function(scope,elem,atrs){
            scope.$watch('fileDownload',function(newValue, oldValue){
            	debugger;
            	
            	var count=localStorage.getItem('count');
            	
                if(newValue!=undefined && newValue!=null){
                    console.debug('Downloading a new file'); 
                    var isFirefox = typeof InstallTrigger !== 'undefined';
                    var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
                    var isIE = /*@cc_on!@*/false || !!document.documentMode;
                    var isEdge = !isIE && !!window.StyleMedia;
                    var isChrome = !!window.chrome && !!window.chrome.webstore;
                    var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
                    var isBlink = (isChrome || isOpera) && !!window.CSS;

                    if(isFirefox || isIE || isChrome){
                    	
                        if(isChrome){
                        	
                            console.log('Manage Google Chrome download');
                            var url = window.URL || window.webkitURL;
                            var fileURL = url.createObjectURL(scope.fileDownload);
                            var downloadLink = angular.element('<a></a>');//create a new  <a> tag element
                            downloadLink.attr('href',fileURL);
                            downloadLink.attr('download',scope.fileName);
                            downloadLink.attr('target','_self');
                            
                            if(count==1){
                            downloadLink[0].click();//call click function
                            url.revokeObjectURL(fileURL);//revoke the object from URL
                            localStorage.setItem('count', 0);                       
                            }
                        }
                        else if(isIE){
                            console.log('Manage IE download>10');
                            window.navigator.msSaveOrOpenBlob(scope.fileDownload,scope.fileName); 
                        }
                        else if(isFirefox){
                            console.log('Manage Mozilla Firefox download');
                            var url = window.URL || window.webkitURL;
                            var fileURL = url.createObjectURL(scope.fileDownload);
                            var a=elem[0];//recover the <a> tag from directive
                            a.href=fileURL;
                            a.download=scope.fileName;
                            a.target='_self';
                            a.click();//we call click function
                        }
                        else{}


                    }else{
                        alert('SORRY YOUR BROWSER IS NOT COMPATIBLE');
                    }
                }
                else{}
            });

        }
    }
})