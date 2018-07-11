resourceApp.controller('addplanCtrl',["$scope","$state","RAService",function($scope, $state, 
								RAService) {
							$scope.$on('$viewContentLoaded', function() {
								
								$scope.plan = {};
							})
							
							$scope.planadd = function() {
								debugger;

								RAService.addplan($scope.plan).then(
										function(data) {
											$scope.plandetails = data.result;
											console.log($scope.plandetails);
											$state.go('RA.plan');
										}, function(err) {
											if (err) {
												$scope.errorMessage = err;
											}
										})
							}

						} ]);