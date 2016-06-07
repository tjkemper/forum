/**
 * @author Taylor Kemper
 */

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