resourceApp.factory('RAService',['$http','$q','APIURL',function($http,$q,APIURL){
	return{
		RAReg: function(pageIndex,pageSizeSelected){
			var deferred = $q.defer();
			$http.get('rest/registration/listRegistrations/'+pageIndex+"/"+pageSizeSelected).success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
		getTopVendorslist: function(){
			
			var deferred = $q.defer();
			$http.get('rest/resource/allVendors').success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
	companieslist: function(){
		debugger;
			debugger;
			var deferred = $q.defer();
			$http.get('rest/registration/listRegistrations').success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
		getTopCustomerslist: function(){
			var deferred = $q.defer();
			$http.get('rest/requirement/allCustomers').success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
		saveRegistration: function(registration){
			debugger;
			var deferred = $q.defer();
			$http.post('rest/registration/createRegistration', registration).success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
		buynow: function(pageIndex,pageSizeSelected){
			var deferred = $q.defer();
			debugger;
		    $http.get('rest/plans/listPlans/'+pageIndex+"/"+pageSizeSelected).success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
		getRegistrationById: function(id){
			var deferred = $q.defer();
			$http.get( 'rest/registration/findOneByPrimaryId/' + id).success(function(response){
					deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			});
			return deferred.promise;
   		},
		updateRegistration: function(registration){
			var deferred = $q.defer();
			$http.put('rest/registration/saveRegistration/' + registration._id, registration).success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
		MenuListnew: function(){ 
			var deferred = $q.defer();
			$http.get('rest/menu/listMenus/1/10').success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
		registrationStatus: function(registration){
			var deferred = $q.defer();
			$http.put('rest/registration/inactiveOrActive/' + registration._id, registration).success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},

        Getfile:function(params){
            debugger;
            console.log("RUNNING");
            var deferred = $q.defer();
            
            $http({
                url:('rest/resource/downloadFile/' + params),             
                method:"GET",//you can use also GET or POST
                data:params,
                headers:{'Content-type': 'application/json'},
                responseType : 'arraybuffer',//THIS IS IMPORTANT
               })
               .success(function (data) {
                   console.debug("SUCCESS");
                   deferred.resolve(data);
               }).error(function (data) {
                    console.error("ERROR");
                    deferred.reject(data);
               });

            return deferred.promise;
           },

   		GetPlanById: function(registrationId){ 
   			debugger;
   			var deferred = $q.defer();
   			$http.get('rest/planMapping/findPlanMappingByRegId/'+registrationId).success(function(response){
   				deferred.resolve(response);
   			}).error(function(err){
   				deferred.reject(err);
   			})
   			return deferred.promise;
   		},
   		
   			
   		changeplan: function(planId){ 
			debugger;
			var deferred = $q.defer();
			$http.get(APIURL +'/ResourceAdda/rest/plans/getplansByPrimaryId/'+planId).success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
		
		paymentcarddetails: function(card){
			debugger;
			var deferred = $q.defer();
			$http.post('rest/payment/createPayment', card).success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
		GetPreviousplan: function(planId){ 
			debugger;
			var deferred = $q.defer();
			$http.get('rest/plans/getPreviousPlansByRegId/'+planId+'/1/10').success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
		
		
		postareqList: function(pageIndex,pageSizeSelected){
			var deferred = $q.defer();
			 $http.get('rest/requirement/listRequirements/'+pageIndex+"/"+pageSizeSelected) .success(function(response){
				 deferred.resolve(response);
			 }).error(function(err){
				 deferred.reject(err);
			 })
			 return deferred.promise;
		},
		postReqfilter: function(filter, value){
	            if(filter.consultant == 0){
	                var deferred = $q.defer();  
	            $http.get('rest/requirement/listRequirements/1/10').then(function(response){
	                deferred.resolve(response);
	            }),function(err){
	                deferred.reject(err);
	            }
	            return deferred.promise;
	            }else{
	                var deferred = $q.defer();
	            $http.get("rest/requirement/findOneAllByCondition/"+filter.jobCategory+'/'+filter.consultant +'/1/10').then(function(response){
	                deferred.resolve(response);
	            }),function(err){
	                deferred.reject(err);
	            }
	            return deferred.promise;
	            }
	            
	        },
	        uploadResumeToUrl : function(uploadFile,uploadUrl){
                debugger;
                var deferred = $q.defer();
                 var fd = new FormData();
                 fd.append('uploadFile', uploadFile);
                 $http.put(uploadUrl, fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                 }).success(function(response){
                 	deferred.resolve(response);
                 }).error(function(err){
                 	deferred.reject(err);
                 })
                 return deferred.promise;
              },
		getCompanyList:function(){
			var deferred = $q.defer();
			$http.get('rest/registration/listRegistrations').success(function(response){
				deferred.resolve(response);
			}).error(function(err){
				deferred.reject(err);
			})
			return deferred.promise;
		},
		adddata: function(postrequirement){
	         var deferred = $q.defer();
	        $http.post('rest/requirement/createRequirement',postrequirement).success(function(response){
	            console.log("success");
	            deferred.resolve(response);
	        }).error(function(err){
	            deferred.reject(err);
	        })
	        return deferred.promise;
	        },
	        postareqGetById: function(id){
	        	var deferred = $q.defer();
	        	$http.get('rest/requirement/findOneByPrimaryId/' + id).success(function(response){
	        		deferred.resolve(response);
	        	}).error(function(err){
	        		deferred.reject(err);
	        	})
	        	return deferred.promise;
	        },

	        uploadFileToUrl : function(uploadFile,uploadUrl){
	            debugger;
	            var deferred = $q.defer();
	             var fd = new FormData();
	             fd.append('uploadFile', uploadFile);
	             $http.put(uploadUrl, fd, {
	                transformRequest: angular.identity,
	                headers: {'Content-Type': undefined}
	             }).success(function(response){
	             	deferred.resolve(response);
	             }).error(function(err){
	             	deferred.reject(err);
	             })
	             return deferred.promise;
	          },
			updatepostareq:function(postrequirement){
				debugger;
				var deferred = $q.defer();
				$http.put('rest/requirement/saveRequirement/',postrequirement).success(function(response){
						deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			requirementStatus : function(postrequirement){
				var deferred = $q.defer();
				$http.put('rest/requirement/inactiveOrActive/' + postrequirement._id, postrequirement).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			registergetcontact: function(){
				var deferred = $q.defer();
				$http.get('rest/address/listAddresses/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			registeraddcontact: function(addcontact){
				var deferred = $q.defer();
				$http.post('rest/address/createAddress', addcontact).success(function(response){
						deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			registergetbyid: function(id){
				var deferred = $q.defer();
				$http.get('rest/address/findOneByPrimaryId/' + id).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			userlist : function(){
				var deferred = $q.defer();
				$http.get('rest/user/listUsers/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			allusergetroles: function(){
				var deferred= $q.defer();
				$http.get('rest/user/getAllRoles/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			alluseradd: function(alluser){
				var deferred = $q.defer();
				$http.post('rest/user/saveUser', alluser).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			allusergetbyid: function(id){
				var deferred = $q.defer();
				$http.get('rest/user/getOneByPrimaryKey/' + id).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			alluserupdate: function(alluser){
				var deferred = $q.defer();
				$http.put('rest/user/updateUser/' + alluser._id, alluser).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			addresourcelist: function(pageIndex,pageSizeSelected){
				debugger;
				var deferred = $q.defer();
				var local = localStorage.getItem("registrationId");
				$http.get('rest/resource/findSowResourcesByRegId/'+local+"/"+pageIndex+"/"+pageSizeSelected).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			
			 uploadResumeToUrl : function(uploadFile,uploadUrl){
                 debugger;
                 var deferred = $q.defer();
                  var fd = new FormData();
                  fd.append('uploadFile', uploadFile);
                  $http.put(uploadUrl, fd, {
                     transformRequest: angular.identity,
                     headers: {'Content-Type': undefined}
                  }).success(function(response){
                  	deferred.resolve(response);
                  }).error(function(err){
                  	deferred.reject(err);
                  })
                  return deferred.promise;
               },
               
			datafilter: function(filter, value){
	            if(filter.consultant == 0){
	                var deferred = $q.defer();  
	            $http.get('rest/resource/listResources/1/10').then(function(response){
	                deferred.resolve(response);
	            }),function(err){
	                deferred.reject(err);
	            }
	            return deferred.promise;
	            }else{
	                var deferred = $q.defer();
	            $http.get('rest/resource/findOneAllByCondition/'+filter.totalExperience+'/'+filter.consultant +'/1/10').then(function(response){
	                deferred.resolve(response);
	            }),function(err){
	                deferred.reject(err);
	            }
	            return deferred.promise;
	            }
	            
	        },
	        
	        RAresourcelist:function(){
	        	debugger;
	        	var deferred=$q.defer();
	        	$http.get('rest/resource/listResources/1/10').then(function(response){
	        		deferred.resolve(response);
	        	}),function(err){
	        		deferred.reject(err);
	        	}
	        	return deferred.promise;
	        },
			addresource: function(resource){
				var deferred = $q.defer();
				$http.post('rest/resource/createResource', resource).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
				
			},
			resourcegetById: function(id){
				var deferred = $q.defer();
				$http.get('rest/resource/findOneByPrimaryId/' + id).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			updateresource: function(resource){
				var deferred = $q.defer();
				$http.put('rest/resource/saveResource/' + resource._id,resource).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			resourceStatus: function(resource){
				var deferred = $q.defer();
				$http.put('rest/resource/inactiveOrActive/' + resource._id, resource).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			postresourceById: function(id){
				var deferred = $q.defer();
				$http.get('rest/resource/findResourcesByRegistrationId/'+ id +'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			postresourceMapping: function(postresource){
				var deferred = $q.defer();
				$http.post('rest/resourceMapping/createResourceMapping', postresource).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			mappingresource: function(filter){
				debugger;
				var deferred = $q.defer();
				$http.get('rest/resource/findResourcesByTwoIds/'+ filter.selecttype +'/'+ filter.nameId +'/'+ filter.localId +'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			
			PostresourceSoft: function(resource){
				var deferred = $q.defer();
				$http.put('rest/resource/softLock/' + resource._id, resource).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			PostresourceHard: function(resource){
				var deferred = $q.defer();
				$http.put('rest/resource/hardLock/' + resource._id, resource).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			addplan: function(plan){
				debugger;
				var deferred = $q.defer();
				$http.post('rest/plans/createPlans', plan).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
				
			},
			plangetById: function(id){
				var deferred = $q.defer();
				$http.get('rest/plans/getplansByPrimaryId/' + id).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			planupdate: function(plan){
				var deferred = $q.defer();
				$http.put('rest/plans/updatePlans/' + plan._id,plan).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			planStatus: function(plan){
				var deferred = $q.defer();
				$http.put('rest/plans/inactiveOrActive/'+ plan._id, plan).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			planlist: function(){
				var deferred = $q.defer();
				debugger;
			    $http.get('rest/plans/listActivePlans/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
//			updatepostareq:function(postId,postrequirement){
//				debugger;
//				var deferred = $q.defer();
//				$http.put(APIURL + "/ResourceAdda/rest/requirement/saveRequirement/" +postId,postrequirement).success(function(response){
//						deferred.resolve(response);
//				}).error(function(err){
//					deferred.reject(err);
//				})
//				return deferred.promise;
//			},
			//vendor services
			venpostareqList: function(pageIndex,pageSizeSelected){
				debugger;
				var deferred = $q.defer();
				var local = localStorage.getItem("registrationId");
				$http.get('rest/requirement/findSowRequirementsByRegId/'+local+'/'+pageIndex+'/'+pageSizeSelected) .success(function(response){
					 deferred.resolve(response);
				 }).error(function(err){
					 deferred.reject(err);
				 })
				 return deferred.promise;
			},
			vendoruserlist : function(local){
				var deferred = $q.defer();
				$http.get('rest/user/findAllByRegistrationId/'+local+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			vendoraddresourcelist: function(pageIndex,pageSizeSelected){
				var deferred = $q.defer();
				var local = localStorage.getItem("registrationId");
				$http.get('rest/resource/findResourcesByRegistrationId/'+local+'/'+pageIndex+'/'+pageSizeSelected).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			vendorgetRegistrationById: function(id){
				var deferred = $q.defer();
				$http.get('rest/registration/findOneByPrimaryId/' + id).success(function(response){
						deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
	   		},
	   		// request resource vendor
	   		requestResourcevendor:function(requirementId,_id,registrationId,vendorId){
				var deferred = $q.defer();
				debugger;
				$http.post('rest/requestResource/reqResource/'+requirementId+'/'+_id+'/'+registrationId+'/'+vendorId).success(function(response){
						deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
	   		},
	   		praposeResourceCustomer:function(requirementId,_id,registrationId,vendorId){
				var deferred = $q.defer();
				debugger;
				$http.post('/ResourceAdda/rest/propsedResource/createProposedResource/'+requirementId+'/'+_id+'/'+registrationId+'/'+vendorId).success(function(response){
						deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
	   		},
	   		vendorpraposedresource:function(id){
	   			var deferred = $q.defer();
	   			
	   			$http.get('rest/propsedResource/findResourcesByCustomerId/'+id+'/1/10').success(function(response){
	   				deferred.resolve(response);
	   			}).error(function(err){
	   				deferred.reject(err);
	   			})
	   			return deferred.promise;
	   		},
	   	
	   			
	  
			//customer service
			customeruserlist : function(local){
				var deferred = $q.defer();
				$http.get('rest/user/findAllByRegistrationId/'+local+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			customeraddrequirment: function(local,pageIndex,pageSizeSelected){
				var deferred = $q.defer();
				$http.get('rest/requirement/findRequirementsByRegistrationId/'+local+'/'+pageIndex+'/'+pageSizeSelected).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			
			viewcustomermsa: function(planId){ 
	   			debugger;
	   			var deferred = $q.defer();
	   			$http.get('rest/registration/listRegisteredSowusersByRegId/'+planId).success(function(response){
	   			deferred.resolve(response);
	   			}).error(function(err){
	   			deferred.reject(err);
	   			})
	   			return deferred.promise;
	   			},
			
			//search			 
			resourcelistaccordion:function(){
				debugger;
				var deferred = $q.defer();
				var reg = localStorage.getItem('registrationId');
				$http.get('rest/resource/allListsByRegId/'+reg).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			requirementlistaccordion:function(){
				debugger;
				var deferred = $q.defer();
				var reg1 = localStorage.getItem('registrationId');
				$http.get('rest/requirement/allListsByRegId/'+reg1).success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			
			allresLists:function(){
				debugger;
				var deferred = $q.defer();
				$http.get('rest/resource/allLists').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			allreqLists:function(){
				debugger;
				var deferred = $q.defer();
				$http.get('rest/requirement/allLists').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
//			getcompany:function(){
//				debugger;
//				var deferred = $q.defer();
//				$http.get(APIURL+'/ResourceAdda/rest/companies/listCompanies').success(function(response){
//					deferred.resolve(response);
//				}).error(function(err){
//					deferred.reject(err);
//				})
//				return deferred.promise;
//			},
//			getbudget:function(){
//				debugger;
//				var deferred = $q.defer();
//				$http.get(APIURL+'/ResourceAdda/rest/budget/litBudget').success(function(response){
//					deferred.resolve(response);
//				}).error(function(err){
//					deferred.reject(err);
//				})
//				return deferred.promise;
//			},
//			getexperience:function(){
//				debugger;
//				var deferred = $q.defer();
//				$http.get(APIURL+'/ResourceAdda/rest/experience/listExperience').success(function(response){
//					deferred.resolve(response);
//				}).error(function(err){
//					deferred.reject(err);
//				})
//				return deferred.promise;
//			},
//			getlocation:function(){
//				debugger;
//				var deferred = $q.defer();
//				$http.get(APIURL+'/ResourceAdda/rest/location/listLocation').success(function(response){
//					deferred.resolve(response);
//				}).error(function(err){
//					deferred.reject(err);
//				})
//				return deferred.promise;
//			},
//			getjobCategory:function(){
//				debugger;
//				var deferred = $q.defer();
//				$http.get(APIURL+'/ResourceAdda/rest/jobCategory/listJobCategory/1/10').success(function(response){
//					deferred.resolve(response);
//				}).error(function(err){
//					deferred.reject(err);
//				})
//				return deferred.promise;
//			},
			menuget:function(role,type){
				debugger;
				var deferred = $q.defer();
				$http.get('rest/menu/findAllByCondition/'+role+'/'+type+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			searchresource:function(skills,jobcategory,location,yearsOfExperience){
				debugger;
				var deferred = $q.defer();
				$http.get('rest/resource/findResourcesBySearch/'+skills+'/'+jobcategory+'/'+location+'/'+yearsOfExperience+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			/*searchsidefilterresource:function(skills,jobcategory,location,experience,vendors,budget){
				debugger;
				var deferred = $q.defer();
				var reg = localStorage.getItem('registrationId');
				$http.get(APIURL+'/ResourceAdda/rest/resource/findResourcesByFilter/'+reg+'/'+skills+'/'+jobcategory+'/'+location+'/'+experience+'/'+vendors+'/'+budget+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},*/
			searchsidefilterrequirment:function(skills,jobcategory,location,experience,vendors,budget){
				debugger;
				var deferred = $q.defer();
				var regid = localStorage.getItem('registrationId');
				$http.get('rest/requirement/findRequirementsBySearchs/'+regid+'/'+skills+'/'+jobcategory+'/'+location+'/'+experience+'/'+vendors+'/'+budget+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			searchsidefilterrequirmentvendor:function(skills,jobcategory,location,experience,vendors,budget){ //edited by Ikhil
				debugger;
				var deferred = $q.defer();
				var customerregid = localStorage.getItem('registrationId');
				$http.get('rest/requirement/findRequirementsByFilter/'+customerregid+'/'+skills+'/'+jobcategory+'/'+location+'/'+experience+'/'+vendors+'/'+budget+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			searchsidefilterresource:function(skills,jobcategory,location,experience,vendors,budget){
				debugger;
				var deferred = $q.defer();
				var regid = localStorage.getItem('registrationId');
				//http://localhost:8080/ResourceAdda/rest/resource/findResourcesBySearchs/registrationId/primarySkills/jobCategory/location/experience/vendors/budget/1/10
				$http.get('rest/resource/findResourcesBySearchs/'+regid+'/'+skills+'/'+jobcategory+'/'+location+'/'+experience+'/'+vendors+'/'+budget+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			searchsidefilterresourcecutomer:function(skills,jobcategory,location,experience,vendors,budget){
				debugger;
				var deferred = $q.defer();
				
				 $http.get('rest/resource/findResourcesByFilter/'+skills+'/'+jobcategory+'/'+location+'/'+experience+'/'+vendors+'/'+budget+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			
			searchrequirement:function(skills,jobcategory,location,experience){
                  debugger;
                  
				var deferred = $q.defer();
				var regid = localStorage.getItem('registrationId');
				$http.get('rest/requirement/findRequirementsBySearch/'+skills+'/'+jobcategory+'/'+location+'/'+experience+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			searchresourcebyid:function(registrationid,skills,jobcategory,location,experience){
				var deferred=$q.defer();
				
				$http.get('rest/resource/findResourcesBySearch/'+registrationid+'/'+skills+'/'+jobcategory+'/'+location+'/'+experience+'/1/10').success(function(response){
				  deferred.resolve(response);	
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
			searchrequirementbyid:function(registrationid,skills,jobcategory,location,experience){
				var deferred=$q.defer();
				$http.get('rest/requirement/findRequirementsBySearch/'+registrationid+'/'+skills+'/'+jobcategory+'/'+location+'/'+experience+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			},
		
			
			customerrequestedresource:function(registrationId){
				var deferred=$q.defer();
				$http.get('rest/requestResource/findResourcesByVendorId/'+registrationId+'/1/10').success(function(response){
					deferred.resolve(response);
				}).error(function(err){
					deferred.reject(err);
				})
				return deferred.promise;
			}
	}

}])
