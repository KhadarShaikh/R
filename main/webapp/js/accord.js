
resourceApp.directive('btstAccordion', function(){
		return {
			restrict: "E",
			replace: true,
			transclude: true,
			template: '<div class="panel-group" ng-transclude></div>',
			link: function(scope,elem, attrs){
				var id = elem.attr("id");

				if (!id) 
				{
					id = "btst_acc" + scope.$id;
					elem.attr("id", id);
				}

				var arr = elem.find(".accordion-toggle");
				for (var i = 0; i < arr.length; i++) {
					$(arr[i]).attr("data-parent", "#" + id);
					$(arr[i]).attr("href", "#" + id + "collapse" + i);
				}

				var arr = elem.find('.panel-collapse');
				$(arr[0]).addClass("in");
				for (var x = 0; x < arr.length; x++) {
					$(arr[x]).attr("id", id + "collapse" + x);
				}

			},
			controller: function(){}
		}}).directive('btstAccordionPanel', function(){
			return {
				require: '^btstAccordion',
				restrict: "E",
				replace: true,
				transclude: true,
				scope: {
					title:"@"
				},
				template: 
				'<div class="panel panel-default">'+
				'	<div class="panel-heading">'+
				'   	<h4 class="panel-title"><a data-toggle="collapse" class="accordion-toggle">{{title}}</a></h4>'+
				'	</div>'+
				'	<div class="panel-collapse collapse">'+
				'   	<div class="panel-body" ng-transclude>'+
				'  		</div>'+
				'	</div>'+
				'</div>',
				link: function(scope,elem,attrs){
					scope.$watch("title", function(){
						var hdr = elem.find(".accordion-toggle");
						hdr.html(scope.title);
					})
				}
			}
		});