package com.ex.controller.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ex.domain.Category;
import com.ex.domain.Message;
import com.ex.domain.Room;
import com.ex.domain.User;
import com.ex.domain.m2m.RoomCategory;
import com.ex.domain.m2m.UserMessage;
import com.ex.service.ForumService;

@RestController
public class ForumRestController {
	
	@Autowired
	private ForumService forumService;
	
	@RequestMapping("user/login")
	public Principal getUser(Principal user) {
		return user;
	}
	
	@RequestMapping(value="user", method=RequestMethod.POST)
	public User getUserDetails(@RequestBody User user){
//		return forumService.auth(user);
		return forumService.getUserDetails(user);
	}
	
	@RequestMapping(value="user", method=RequestMethod.PUT)
	public User registerUser(@RequestBody User user){
		return forumService.registerUser(user);
	}
	
	@RequestMapping(value="user", method=RequestMethod.PATCH)
	public User updateUser(@RequestBody User user){
		return forumService.updateUser(user);
	}
	
	@RequestMapping(value="rooms", method=RequestMethod.GET)
	public List<Room> rooms(){
		return forumService.getRooms();
	}
	
	@RequestMapping(value="room", method=RequestMethod.POST)
	public void createRoom(@RequestBody Room room){
		forumService.createRoom(room);
	}
	
	@RequestMapping(value="room/{roomName}", method=RequestMethod.DELETE)
	public void closeRoom(@PathVariable String roomName){
		forumService.closeRoom(roomName);
	}
	
//	@RequestMapping(value="room/{roomName}/messages", method=RequestMethod.GET)
//	public List<Message> roomMessages(@PathVariable String roomName){
//		return forumService.getMessagesByRoom(roomName);
//	}
	
	@RequestMapping(value="room/{roomName}/messages", method=RequestMethod.GET)
	public Page<Message> roomMessages(@PathVariable String roomName, Integer page, Integer size){
		return forumService.getMessagesByRoomPage(roomName, page, size);
	}
	
	@RequestMapping(value="room/{roomName}/messages", method=RequestMethod.POST)
	public void postMessage(@RequestBody Message message, @PathVariable String roomName){
		forumService.postMessage(message, roomName);
	}
	
	
	@RequestMapping(value="message/{messageId}/like", method=RequestMethod.POST)
	public UserMessage likeMessage(@RequestBody UserMessage userMessage, @PathVariable String messageId){
		return forumService.likeMessage(userMessage, messageId);
	}
	
	@RequestMapping(value="category", method=RequestMethod.POST)
	public Category createCategory(@RequestBody Category category){
		return forumService.createCategory(category);
	}
	
	@RequestMapping(value="room/{roomName}/category", method=RequestMethod.PUT)
	public RoomCategory addCategoryToRoom(@RequestBody RoomCategory roomCategory, @PathVariable String roomName){
		return forumService.addCategoryToRoom(roomCategory, roomName);
	}
	
	@RequestMapping(value="room/{roomName}/category", method=RequestMethod.DELETE)
	public Integer removeCategoryFromRoom(@RequestBody RoomCategory roomCategory, @PathVariable String roomName){
		
		return forumService.removeCategoryFromRoom(roomCategory, roomName);
	}
	
	
}
