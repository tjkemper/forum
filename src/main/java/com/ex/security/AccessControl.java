package com.ex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ex.domain.Message;
import com.ex.domain.Room;
import com.ex.repo.MessageRepo;
import com.ex.repo.RoomRepo;

@Component
public class AccessControl {
	
	@Autowired
	private RoomRepo roomRepo;
	
	@Autowired
	private MessageRepo messageRepo;
	
	//
	/*
	 * updateUser
	 * PATCH user/{username}
	 */
	public boolean checkUserIsUser(Authentication authentication, String username){
		if(authentication.getName().equals(username)){
			return true;
		}else {
			return false;
		}
	}
	
	//
	/*
	 * updateMessage
	 * PUT message/{messageId}
	 */
	/*
	 * deleteMessage
	 * DELETE message/{messageId}
	 */
	public boolean checkUserOwnsMessage(Authentication authentication, int messageId){
		System.out.println("checkUserOwnsMessage");
		System.out.println(authentication);
		System.out.println(messageId);
		Message message = messageRepo.findOne(messageId);
		if (message != null && authentication.getName().equals(message.getOwner().getUsername())) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	//room/{roomName}
	//room/{roomName}/updateRoomName
	//room/{roomName}/updateRoomDescription
	//room/{roomName}/category
	
	/*
	 * updateRoomName
	 * PUT room/{roomName}/updateRoomName
	 */
	
	/*
	 * updateRoomDescription
	 * PUT room/{roomName}/updateRoomDescription
	 */
	
	/*
	 * closeRoom
	 * DELETE room/{roomName}
	 */
	
	/*
	 * reopenRoom
	 * PUT room/{roomName}
	 */

	/*
	 * addCategoryToRoom
	 * PUT room/{roomName}/category
	 */
	/*
	 * removeCategoryFromRoom
	 * DELETE room/{roomName}/category
	 */
	public boolean checkUserOwnsRoom(Authentication authentication, String roomName){
		System.out.println("checkUserOwnsRoom");
		System.out.println(authentication);
		System.out.println(roomName);
		Room room = roomRepo.findOneByName(roomName);
		if(room != null && authentication.getName().equals(room.getOwner().getUsername())){
			return true;
		}else {
			return false;
		}
	}
	
	
}
