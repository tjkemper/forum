/**
 * @author Taylor Kemper
 */

angular.module("ForumApp",["ui.router", "ui.bootstrap", 'ngAnimate', 'ui.gravatar']);

//angular.module("ForumApp").constant("baseUrl","http://localhost:8085/forum/");
angular.module("ForumApp").constant("userUrl","user/");
angular.module("ForumApp").constant("loginUrl","login/");
angular.module("ForumApp").constant("logoutUrl","logout/");
angular.module("ForumApp").constant("roomsUrl","rooms/");
angular.module("ForumApp").constant("roomUrl","room/");
angular.module("ForumApp").constant("messagesUrl","/messages/");

angular.module("ForumApp")
.config(function($stateProvider, $urlRouterProvider){
	
    $urlRouterProvider.otherwise('/home');
    
    $stateProvider
        .state('logoutState', {
            url: '/logout',
            controller:"LogoutCtrl"
        })
        .state('homeState', {
            url: '/home',
            templateUrl: 'ng/templates/home.html',
            controller: 'HomeCtrl as hData'
        })
        .state('allRoomsState', {
            url: '/room',
            templateUrl: 'ng/templates/allRooms.html',
            controller:'AllRoomsCtrl as aData'
        })
        .state('roomState', {
            url: '/room/:roomName',
            templateUrl: 'ng/templates/room.html',
            controller:'RoomCtrl as rData'
        });
});

angular.module("ForumApp")
.controller("NavbarCtrl", function(ForumService, $uibModal){
	
	var navbarData = this;
	navbarData.authUser = ForumService.authUser;
	
	navbarData.openUserModal = function(){
	    var modalInstance = $uibModal.open({
	        animation: true,
	        templateUrl: 'ng/templates/modals/userModal.html',
	        controller: 'UserModalCtrl as uData',
	        size: 'lg'
	      });	
	}

	navbarData.openLoginModal = function(){
	    var modalInstance = $uibModal.open({
	        animation: true,
	        templateUrl: 'ng/templates/modals/login.html',
	        controller: 'LoginCtrl as lData',
	        size: 'sm'
	      });	
	}
	
	navbarData.openRegisterModal = function(){
	    var modalInstance = $uibModal.open({
	        animation: true,
	        templateUrl: 'ng/templates/modals/register.html',
	        controller: 'RegisterCtrl as rData',
	        size: 'sm'
	      });	
	}
	
});

angular.module("ForumApp")
.controller("UserModalCtrl", function(ForumService, $uibModalInstance){
	
	var userModalData = this;
	
	//Just the userDetails
	if(ForumService.authUser.userDetails){
		userModalData.userDetails = ForumService.copyUserDetails();
	}else{
		var promise = ForumService.getUserDetails(ForumService.authUser.name);
		promise.then(
				function(response){
					userModalData.userDetails = ForumService.copyUserDetails();
				}, function(response){
			
				});
	}
	userModalData.update = function () {
//		$uibModalInstance.close();
		
		
		var promise = ForumService.updateUser(userModalData.userDetails);
		
		promise.then(function(response){
			console.log("request success");
			if(response.data != null && response.data != ""){
				console.log("update user success");
				//ForumService.setAuthUser(response.data);
				$uibModalInstance.close();
			}
		}, function(response){
			console.log("request error");
		});
		
		
		
	};
	userModalData.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
});

angular.module("ForumApp")
.controller("HomeCtrl", function(ForumService){
	
	var homeData = this;
	homeData.stats = ForumService.getStats();
	
});

angular.module("ForumApp")
.controller("LogoutCtrl", function($state, ForumService){
	
	ForumService.logout();
	$state.go("homeState");
	
});

angular.module("ForumApp")
.controller("LoginCtrl", function(ForumService, $state, $uibModalInstance){
	
	var loginData = this;
	
	loginData.unauthUser = {};
	loginData.message = "";
	
	loginData.auth = function(){
		var promise = ForumService.auth(loginData.unauthUser);
		
		promise.then(function(response) {
				if(response.data){
					ForumService.setAuthUser(response.data);
					$uibModalInstance.close();

					$state.go("allRoomsState");
				}
			}, function(response) {
				console.log("LoginCtrl - request error");
				loginData.message = "Username/Password incorrect.";
			});
	}
	
});

angular.module("ForumApp")
.controller("RegisterCtrl", function(ForumService, $state, $uibModalInstance){
	
	var registerData = this;
	
	registerData.newUser = {};
	
	registerData.register = function(){
		
		var promise = ForumService.register(registerData.newUser);
		
		promise.then(function(response){
			console.log("request success");
			if(response.data != null && response.data != ""){
				console.log("register success");
				//ForumService.setAuthUser(response.data);
				$uibModalInstance.close();

				$state.go("allRoomsState");
			}
		}, function(response){
			console.log("request error");
		});
		
		
	}
	
});

angular.module("ForumApp")
.controller("AllRoomsCtrl", function($http, ForumService, $state){
	
	var allRoomsData = this;
	
	allRoomsData.authUser = ForumService.authUser;
	
	allRoomsData.allRooms = ForumService.allRooms;
	
	allRoomsData.newRoom = null;
	
	ForumService.getAllRooms();
	
	allRoomsData.viewRoom = function(roomName){
		$state.go("roomState", {roomName:roomName});
	}
	
	allRoomsData.createRoom = function(){
		if(allRoomsData.authUser){
			
			allRoomsData.newRoom.owner = {
											username : allRoomsData.authUser.name					
										 }

			ForumService.createRoom(allRoomsData.newRoom).then(function(response){
				allRoomsData.newRoom = null;
				ForumService.getAllRooms();
			}, function(response){});
		}
	}
	
	allRoomsData.closeRoom = function(roomName){
		ForumService.closeRoom(roomName).then(function(response){
			
			ForumService.getAllRooms();
			
		},function(response){});
	}
	
});

angular.module("ForumApp")
.controller("RoomCtrl", function($http, ForumService, $state, $stateParams){
	
	var roomData = this;
	
	roomData.authUser = ForumService.authUser;	
	roomData.currentRoom = ForumService.currentRoom;
	roomData.currentMessages = ForumService.currentMessages;
	roomData.lastPageAccessed = ForumService.lastPageAccessed;
	roomData.newMessage="";
	roomData.readyForMorePosts = true;
	
	var roomName = $stateParams.roomName;
	ForumService.getRoom(roomName);
	
	roomData.postMessage = function(){
		ForumService.postMessage(roomData.newMessage);
		roomData.newMessage="";
	}
	
	roomData.loadMoreMessages = function(){
		if(roomData.readyForMorePosts){
			ForumService.getSetRoomMessages(roomName, false, roomData.lastPageAccessed.number + 1, roomData.lastPageAccessed.size)
			.then(function(response){
				roomData.readyForMorePosts = true;
			}, function(response){});
		}
		//TODO: set Load More directive if NOT last page
	}
	
	roomData.closeRoom = function(roomName){
		ForumService.closeRoom(roomName).then(function(response){
			ForumService.getSetCurrentRoom(roomName, true);
		},function(response){});
	}
	
});

angular.module("ForumApp")
.service("ForumService", function($http, userUrl, loginUrl, logoutUrl, roomsUrl, roomUrl, messagesUrl){
	
	var serviceData = this;
	
	serviceData.authUser = {};
	serviceData.allRooms = [];
	serviceData.currentRoom = {};
	serviceData.currentMessages = [];
	serviceData.lastPageAccessed = {};
	
	
	
	serviceData.getStats = function(){
		//TODO: implement
	}
	
	serviceData.auth = function(user){
		var headers = user ? {
			Authorization : "Basic "
					+ btoa(user.username + ":" + user.password)
		} : {};
		return $http({
			method:'POST',
			url:userUrl+loginUrl,
			headers:headers
		}).then(function(response){
			return response;
		}, function(response){
			throw response;
		});

	}
	
	serviceData.getUserDetails = function(username){
		return $http({
			method:'POST',
			url:userUrl,
			data:{
				username:username
			}
		}).then(function(response){
			console.log('ForumService - success getUserDetails');
			serviceData.authUser.userDetails = response.data;
		}, function(response){
			console.log('ForumService - failed getUserDetails');
		});
	}
	
	serviceData.register = function(user){
		return $http({
			method:'PUT',
			url:userUrl,
			data:user
		}).then(function(response){
			serviceData.auth(user);
			return response;
		}, function(response){});
	}
	
	serviceData.updateUser = function(userDetails){
		return $http({
			method:'PATCH',
			url:userUrl,
			data:userDetails		
		}).then(function(response){
			//serviceData.auth();//TODO: will it work w/o credentials?
			serviceData.getUserDetails(serviceData.authUser.name);
			return response;
		}, function(response){});
	}
	
	serviceData.setAuthUser = function(someUser){
		setPropsDynamically(someUser, serviceData.authUser);
	}
	
	serviceData.copyAuthUser = function(){
		var authUserCopy = {};
		setPropsDynamically(serviceData.authUser, authUserCopy);
		return authUserCopy;
	}
	
	serviceData.copyUserDetails = function(){
		var userDetailsCopy = {};
		setPropsDynamically(serviceData.authUser.userDetails, userDetailsCopy);
		return userDetailsCopy;
	}
	
	
	serviceData.logout = function(){
		return $http({
			method:'POST',
			url:logoutUrl
		}).then(function(response){
			clearPropsDynamically(serviceData.authUser);	
		}, function(response){});
		
	}
	
	serviceData.getAllRooms = function(){
		return $http({
			method:'GET',
			url:roomsUrl
		}).then(function(response){
			serviceData.allRooms.length = 0;
			Array.prototype.push.apply(serviceData.allRooms, response.data);
		},function(response){});
	}
	
	serviceData.createRoom = function(room){
		return $http({
			method:'POST',
			url:roomUrl,
			data:room
		});
	}
	
	
	serviceData.getRoom = function(roomName){
		
		serviceData.getSetCurrentRoom(roomName);
		serviceData.getSetRoomMessages(roomName, true);
		
	}
	
	var getSetCurrentRoomHelper = function(roomName){
		for(var arIndex = 0; arIndex < serviceData.allRooms.length; arIndex++){
			if(roomName == serviceData.allRooms[arIndex].name){
				
				setPropsDynamically(serviceData.allRooms[arIndex], serviceData.currentRoom);
				break;
			}
		}
	} 
	
	serviceData.getSetCurrentRoom = function(roomName, reset){
		
		if(serviceData.allRooms.length == 0 || reset){
			var subpromise = serviceData.getAllRooms();
			subpromise.then(function(){
				getSetCurrentRoomHelper(roomName);	
			},function(){});
		}else {
			getSetCurrentRoomHelper(roomName);
		}
	}
	
	
	serviceData.getSetRoomMessages = function(roomName, reset, page, size){
		if(reset){
			serviceData.currentMessages.length = 0;
		}
		
		page = page || 0; //Default page number (base 0)
		size = size || 10; //Default page size
		
		return $http({
			method:'GET',
			url:roomUrl+roomName+messagesUrl + "?"+"page="+page+"&"+"size="+size
		}).then(function(response){

			Array.prototype.push.apply(serviceData.currentMessages, response.data.content);
			setPropsDynamically(response.data, serviceData.lastPageAccessed);
			
		},function(response){});
	}
	
	serviceData.postMessage = function(message){
		var postData = {
			message:message,
			owner:{
				username:serviceData.authUser.name
			}
		};
		
		$http({
			method:'POST',
			url:roomUrl+serviceData.currentRoom.name+messagesUrl,
			data:postData
		}).then(function(response){
			serviceData.getSetRoomMessages(serviceData.currentRoom.name, true);
		},function(response){});
	}
	
	
	serviceData.closeRoom = function(roomName){
		return $http({
			method:'DELETE',
			url:roomUrl + roomName,
		});
	}
	
	
	/*
	 * Helper fn
	 */
	var clearPropsDynamically = function(obj){
		for(var prop in obj){
			if(obj.hasOwnProperty(prop)){
				delete obj[prop];
			}
		}
	}
	
	var setPropsDynamically = function(from, to){
		clearPropsDynamically(to);
		
		for(var fromProp in from){
			if(from.hasOwnProperty(fromProp)){
				to[fromProp] = from[fromProp];
			}
		}
	}
	
	/*
	 * Init user iff not found
	 */
	if(!serviceData.authUser.name){
		var promise = serviceData.auth();
		promise.then(function(response) {
			if(response.data){
				serviceData.setAuthUser(response.data);
			}
		}, function(response) {});		
	}

});

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
			
			var scrollElement = angular.element(element[0].querySelector('#scroll'));
			
			scrollElement[0].addEventListener('click', function(event){
				scrollTo(0,0);
//				angular.element().animate({scrollTop: 0}, "slow");
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



