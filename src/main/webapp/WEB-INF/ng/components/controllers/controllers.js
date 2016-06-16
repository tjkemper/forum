/**
 * @author Taylor Kemper
 */

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
				loginData.message = "Username/Password incorrect.";
			});
	}
	
});

angular.module("ForumApp")
.controller("RegisterCtrl", function(ForumService, $state, $uibModalInstance){
	
	var registerData = this;
	
	registerData.newUser = {};
	registerData.message = "";
	
	registerData.register = function(){
		
		var promise = ForumService.register(registerData.newUser);
		
		promise.then(function(response){

			if(response.data != null && response.data != ""){
				
				//iff register success then authenticate user
				var authpromise = ForumService.auth(registerData.newUser);
				authpromise.then(function(response) {
						if(response.data){
							ForumService.setAuthUser(response.data);
							$uibModalInstance.close();
							$state.go("allRoomsState");
						}
					}, function(response) {});
			}
		}, function(response){
			registerData.message = "username already exists";
		});
		
	}
	
});

angular.module("ForumApp")
.controller("AllRoomsCtrl", function($http, ForumService, $state){
	
	var allRoomsData = this;
	
	allRoomsData.authUser = ForumService.authUser;
	
	allRoomsData.allRooms = ForumService.allRooms;
	allRoomsData.newRoom = null;
	allRoomsData.message = "";

	allRoomsData.currentRooms = [];
	allRoomsData.roomFilter = {};
	allRoomsData.lastRoomPage = {};
	allRoomsData.readyForMoreRooms = false;
	
	var getRoomPagePromise = ForumService.getRoomPage();
	getRoomPagePromise.then(function(response){
		
		handleRoomResponse(response);
		
	}, function(response){});
	
	allRoomsData.loadMoreRooms = function(){
		if(allRoomsData.readyForMoreRooms){
			ForumService.getRoomPage(allRoomsData.lastRoomPage.number + 1, allRoomsData.lastRoomPage.size)
			.then(function(response){
				
				handleRoomResponse(response);
				
			}, function(response){});
		}
		
	}
	
	var handleRoomResponse = function(response){
		Array.prototype.push.apply(allRoomsData.currentRooms, response.data.content);
		ForumService.setPropsDynamically(response.data, allRoomsData.lastRoomPage);
		allRoomsData.readyForMoreRooms = true;
	}
	
//	ForumService.getAllRooms();
	
	allRoomsData.viewRoom = function(roomName){
		$state.go("roomState", {roomName:roomName});
	}
	
	allRoomsData.createRoom = function(){
		
		allRoomsData.message = "";
		
		if(allRoomsData.authUser){
			
			allRoomsData.newRoom.owner = {
											username : allRoomsData.authUser.name					
										 }

			ForumService.createRoom(allRoomsData.newRoom).then(function(response){
				allRoomsData.newRoom = null;
				ForumService.getAllRooms();
			}, function(response){
				allRoomsData.message = "Room name already exists."
			});
		}
	}
	
	allRoomsData.closeRoom = function(roomName){
		ForumService.closeRoom(roomName).then(function(response){
			
			ForumService.getAllRooms();
			
		},function(response){});
	}
	
	allRoomsData.reopenRoom = function(roomName){
		ForumService.reopenRoom(roomName).then(function(response){
			
			ForumService.getAllRooms();
			
		},function(response){});
	}
	
});

angular.module("ForumApp")
.controller("RoomCtrl", function($http, ForumService, $state, $stateParams){
	
	var roomData = this;
	
	roomData.authUser = ForumService.authUser;	
	roomData.currentRoom = {};
	roomData.newMessage="";
	
	//handle the loading of messages
	roomData.currentMessages = [];
	roomData.lastPageAccessed = {};
	roomData.readyForMorePosts = false;
	
	var roomName = $stateParams.roomName;
	
	var getRoomPromise = ForumService.getRoomByName(roomName);
	getRoomPromise.then(function(response){
		ForumService.setPropsDynamically(response.data, roomData.currentRoom);
	}, function(response){
		
	});
	
	ForumService.getMessagePage(roomName)
	.then(function(response){
		Array.prototype.push.apply(roomData.currentMessages, response.data.content);
		ForumService.setPropsDynamically(response.data, roomData.lastPageAccessed);
		roomData.readyForMorePosts = true;
	}, function(response){
		
	});
	
	
	roomData.postMessage = function(){
		var postPromise = ForumService.postMessage(roomData.currentRoom.name, roomData.newMessage);
		
		postPromise.then(function(response){
			
			roomData.currentMessages.unshift(response.data);
			roomData.newMessage="";
			
		},function(response){
			
		});	
	}
	
	roomData.updateMessage = function(msg, newMessage){
		var promise = ForumService.updateMessage(msg, newMessage);
		promise.then(function(response){
			msg.message = newMessage;
		}, function(response){
			
		});
	}
	
	roomData.deleteMessage = function(msg){
		var promise = ForumService.deleteMessage(msg);
		promise.then(function(response){
			roomData.currentMessages.splice(roomData.currentMessages.indexOf(msg), 1);
		}, function(response){
			
		});
	}
	
	roomData.loadMoreMessages = function(){
		if(roomData.readyForMorePosts){
			ForumService.getMessagePage(roomName, roomData.lastPageAccessed.number + 1, roomData.lastPageAccessed.size)
			.then(function(response){
				Array.prototype.push.apply(roomData.currentMessages, response.data.content);
				ForumService.setPropsDynamically(response.data, roomData.lastPageAccessed);
				roomData.readyForMorePosts = true;
			}, function(response){});
		}
	}
	
	roomData.updateRoomName = function(room, newName){
		if(newName) {
			var promise = ForumService.updateRoomName(room, newName);
			
			promise.then(function(response){
				room.name = newName;
				$state.go("roomState", {roomName:newName});
			}, function(response){
				console.log('updateRoomName failed')
				console.log('\tfrom ' + room.name + ' to ' + newName);
			});
		}
	}
	
	roomData.updateRoomDescription = function(room, newDescription){
		var promise = ForumService.updateRoomDescription(room, newDescription);
		
		promise.then(function(response){
			room.description = newDescription;
		}, function(response){
			console.log('updateRoomDescription failed')
			console.log('\tfrom ' + room.description + ' to ' + newDescription);
		});
	}
	
	
	roomData.closeRoom = function(roomName){
		ForumService.closeRoom(roomName).then(function(response){
			
//			ForumService.setPropsDynamically(response.data, roomData.currentRoom);
			roomData.currentRoom.closed = Date.now();
			
		},function(response){});
	}
	
	roomData.reopenRoom = function(roomName){
		ForumService.reopenRoom(roomName).then(function(response){
		
//			ForumService.setPropsDynamically(response.data, roomData.currentRoom);
			roomData.currentRoom.closed = null;

		},function(response){});
	}
	
	roomData.likeMessage = function(message, userLikesMessage){
		
		var promise = ForumService.likeMessage(message.id, userLikesMessage);
		
		promise.then(function(response){
			message.authUserMessage = response.data;
			if(userLikesMessage){
				message.numLikes++;
			}else {
				message.numLikes--;
			}
		}, function(response){});
	}
	
	roomData.addCategoryToRoom = function(room, category){
		var categoryFormatted = {
				category : {
					categoryName : category
				}
		}
		ForumService.addCategoryToRoom(room, categoryFormatted);
	}
	
	
	roomData.removeCategoryFromRoom = function(room, category){
		ForumService.removeCategoryFromRoom(room, category);
	}
	
});
