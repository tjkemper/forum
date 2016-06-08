/**
 * @author Taylor Kemper
 */


angular.module("ForumApp")
.directive("forumRoom", function(){
	return {
		scope : {
			room : '=',
			username : '=',
			closefn : '&'
		},
		templateUrl: 'ng/templates/directives/forumRoom.html'
	};
});

angular.module("ForumApp")
.directive("scrollCallback", function(){
	return {
		scope : {
			scrollThreshold : '=',
			readyForMore : '=',
			cb : '&'
		},
		templateUrl: 'ng/templates/directives/scrollCallback.html',
		link : function(scope, element, attrs){

			var scrollThreshold = attrs.scrollThreshold || 0;
			
			angular.element(document).bind('scroll', function(){
				var elementHeight = element[0].offsetTop;
				var windowBottom = scrollY + innerHeight;

				if(elementHeight && scope.readyForMore && (windowBottom - scrollThreshold > elementHeight)){
					scope.readyForMore = false;
					scope.cb();
				}
			});
		}
	};
});

angular.module("ForumApp")
.directive("scrollToTop", function(){
	return {
		scope : {
			
		},
		templateUrl: "ng/templates/directives/scrollToTop.html",
		link: function(scope, element, attrs){
			
			function scrollToTop(scrollDuration) {
			    const   scrollHeight = window.scrollY,
			            scrollStep = Math.PI / ( scrollDuration / 15 ),
			            cosParameter = scrollHeight / 2;
			    var     scrollCount = 0,
			            scrollMargin;
			    requestAnimationFrame(step);        
			    function step () {
			        setTimeout(function() {
			            if ( window.scrollY != 0 ) {
			                    requestAnimationFrame(step);
			                scrollCount = scrollCount + 1;  
			                scrollMargin = cosParameter - cosParameter * Math.cos( scrollCount * scrollStep );
			                window.scrollTo( 0, ( scrollHeight - scrollMargin ) );
			            }
			        }, 15 );
			    }
			}	
		
			var scrollElement = angular.element(element[0].querySelector('#scroll'));
			
			scrollElement[0].addEventListener('click', function(event){
				scrollToTop(100);
			});
			
			angular.element(document).bind('scroll', function() {
				
			    var currentYPos = scrollY; 						//current y position
			    var bodyHeight = document.body.clientHeight; 	//total height of <body>
			    var windowInnerHeight = innerHeight; 			//height of inner window

			    var scrollPercentage = currentYPos / (bodyHeight - windowInnerHeight);

			    var maxTop = 180;
			    var scrollElementHeight = 30; //because
			    var newTop = scrollPercentage * maxTop - scrollElementHeight;
			    scrollElement[0].style.top = newTop+'px';
			    
			});
		}
	};
});