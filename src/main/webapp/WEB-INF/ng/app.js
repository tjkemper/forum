/**
 * @author Taylor Kemper
 */

angular.module("ForumApp",["ui.router", "ui.bootstrap", 'ngAnimate', 'ui.gravatar']);

//angular.module("ForumApp").constant("baseUrl","http://localhost:8085/forum/");
angular.module("ForumApp").constant("userUrl","user");
angular.module("ForumApp").constant("roomsUrl","rooms");
angular.module("ForumApp").constant("roomUrl","room/");
angular.module("ForumApp").constant("messagesUrl","/messages");

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
	
	userModalData.authUser = ForumService.copyAuthUser();
	
	userModalData.update = function () {
//		$uibModalInstance.close();
		
		var promise = ForumService.updateUser(userModalData.authUser);
		
		promise.then(function(response){
			console.log("request success");
			if(response.data != null && response.data != ""){
				console.log("update user success");
				ForumService.setAuthUser(response.data);
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
	
	loginData.auth = function(){
		var promise = ForumService.auth(loginData.unauthUser);
		
		promise.then(function(response) {
				console.log("request success");
				if(response.data != null && response.data != ""){
					console.log("auth success");
					ForumService.setAuthUser(response.data);
					$uibModalInstance.close();

					$state.go("allRoomsState");
				}
			}, function(response) {
				console.log("request error");
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
				ForumService.setAuthUser(response.data);
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
											username : allRoomsData.authUser.username					
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
	
	var roomName = $stateParams.roomName;
	ForumService.getRoom(roomName);
	
	roomData.postMessage = function(){
		ForumService.postMessage(roomData.newMessage);
		roomData.newMessage="";
	}
	
	roomData.loadMoreMessages = function(){
		ForumService.getSetRoomMessages(roomName, false, roomData.lastPageAccessed.number + 1, roomData.lastPageAccessed.size);
	}
	
	roomData.closeRoom = function(roomName){
		ForumService.closeRoom(roomName).then(function(response){
			ForumService.getSetCurrentRoom(roomName, true);
		},function(response){});
	}
	
});

angular.module("ForumApp")
.service("ForumService", function($http, userUrl, roomsUrl, roomUrl, messagesUrl){
	
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
		return $http({
			method:'POST',
			url:userUrl,
			data:user
		});
	}
	
	serviceData.register = function(user){
		return $http({
			method:'PUT',
			url:userUrl,
			data:user
		});
	}
	
	serviceData.updateUser = function(user){
		return $http({
			method:'PATCH',
			url:userUrl,
			data:user
//			data:{
//				username:serviceData.username,
//				email:email,
//				firstName:firstName,
//				lastName:lastName
//			}
				
		});
	}
	
	serviceData.setAuthUser = function(someUser){
		setPropsDynamically(someUser, serviceData.authUser);
//		serviceData.authUser.username = someUser.username;
//		serviceData.authUser.email = someUser.email;
	}
	
	serviceData.copyAuthUser = function(){
		var authUserCopy = {};
		setPropsDynamically(serviceData.authUser, authUserCopy);
		return authUserCopy;
	}
	
	serviceData.logout = function(){
		clearPropsDynamically(serviceData.authUser);
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
		size = size || 3; //Default page size
		
		$http({
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
				username:serviceData.authUser.username
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



