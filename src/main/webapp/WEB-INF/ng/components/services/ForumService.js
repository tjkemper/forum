/**
 * @author Taylor Kemper
 */

angular.module("ForumApp")
.service("ForumService", function($http, $cookies, userUrl, ownerUrl, loginUrl, logoutUrl, roomsUrl, roomUrl, messagesUrl, messageUrl, likeUrl, categoryUrl, updateRoomNameUrl, updateRoomDescriptionUrl){
	
	var serviceData = this;
	
	serviceData.authUser = {};
	serviceData.allRooms = [];
	
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
			return response;
		}, function(response){
			throw response;
		});
	}
	
	serviceData.updateUser = function(userDetails){
		return $http({
			method:'PATCH',
			url:ownerUrl+userUrl+userDetails.username,
			data:userDetails		
		}).then(function(response){
			//serviceData.auth();//TODO: will it work w/o credentials?
			serviceData.getUserDetails(serviceData.authUser.name);
			return response;
		}, function(response){});
	}
	
	serviceData.setAuthUser = function(someUser){
		serviceData.setPropsDynamically(someUser, serviceData.authUser);
	}
	
	serviceData.copyAuthUser = function(){
		var authUserCopy = {};
		serviceData.setPropsDynamically(serviceData.authUser, authUserCopy);
		return authUserCopy;
	}
	
	serviceData.copyUserDetails = function(){
		var userDetailsCopy = {};
		serviceData.setPropsDynamically(serviceData.authUser.userDetails, userDetailsCopy);
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
	
	//TODO: refactor for pagination
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
	
	serviceData.getRoomByName = function(roomName){
		return $http({
			method:'GET',
			url:roomUrl+roomName
		});
	}
	
	serviceData.getRoomPage = function(roomFilter, page, size){
		
		roomFilter = roomFilter || {}; // default roomFilter is empty object
		
		page = page || 0; //Default page number (base 0)
		size = size || 10; //Default page size
		
		return $http({
			method:'POST',
			url:roomsUrl + "?"+"page="+page+"&"+"size="+size,
			data:roomFilter
		});
		
	}
	
	serviceData.getMessagePage = function(roomName, page, size){
		
		page = page || 0; //Default page number (base 0)
		size = size || 10; //Default page size
		
		return $http({
			method:'GET',
			url:roomUrl+roomName+messagesUrl + "?"+"page="+page+"&"+"size="+size
		});
		
	}
	
	
	serviceData.postMessage = function(roomName, message){
		var postData = {
			message:message,
			owner:{
				username:serviceData.authUser.name
			}
		};
		
		return $http({
			method:'POST',
			url:roomUrl+roomName+messagesUrl,
			data:postData
		});
	}
	
	serviceData.updateRoomName = function(room, newName){
		return $http({
					method:'PUT',
					url:ownerUrl+roomUrl+room.name+updateRoomNameUrl+'?newRoomName='+newName,
		});
	}
	
	serviceData.updateMessage = function(msg, newMessage){
		return $http({
			method:'PUT',
			url:ownerUrl+messageUrl + '/' + msg.id,
			data:newMessage
		});
	}
	
	serviceData.deleteMessage = function(msg){
		return $http({
			method:'DELETE',
			url:ownerUrl+messageUrl + '/' + msg.id
		});
	}
	
	serviceData.updateRoomDescription = function(room, newDescription){
		return $http({
					method:'PUT',
					url:ownerUrl+roomUrl+room.name+updateRoomDescriptionUrl,
					data:newDescription
		});
	}
	
	
	serviceData.closeRoom = function(roomName){
		return $http({
			method:'DELETE',
			url:ownerUrl+roomUrl + roomName,
		});
	}
	
	serviceData.reopenRoom = function(roomName){
		return $http({
			method:'PUT',
			url:ownerUrl+roomUrl + roomName,
		});
	}
	
	serviceData.likeMessage = function(messageId, userLikesMessage){
		
		var data = {
			user:{
				username : serviceData.authUser.name
			},
			userLikesMessage:userLikesMessage
		};
		
		return $http({
			method:'POST',
			url:messageUrl + '/' + messageId + likeUrl,
			data:data
		});
	}
	
	serviceData.addCategoryToRoom = function(room, category){
		return $http({
			method:'PUT',
			url:ownerUrl+roomUrl+room.name+categoryUrl,
			data:category
		}).then(function(response){
			if(response.data){
				room.roomCategory.push(response.data);
			}
			return response;
		}, function(response){
			throw response;
		});
	}
	
	serviceData.removeCategoryFromRoom = function(room, category){
		return $http({
			method:'DELETE',
			url:ownerUrl+roomUrl+room.name+categoryUrl,
			data:category,
            headers: {
                'Content-Type': 'application/json'
            }
		}).then(function(response){
			
			var categoryIndex = room.roomCategory.indexOf(category);
			room.roomCategory.splice(categoryIndex, 1);

			return response;
		}, function(response){
			throw response;
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
	
	serviceData.setPropsDynamically = function(from, to){
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