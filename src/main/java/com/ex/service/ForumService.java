package com.ex.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ex.domain.Category;
import com.ex.domain.Message;
import com.ex.domain.Room;
import com.ex.domain.User;
import com.ex.domain.m2m.RoomCategory;
import com.ex.domain.m2m.UserMessage;
import com.ex.model.RegisterUser;
import com.ex.model.RoomFilter;

public interface ForumService {
	
	public User getUserDetails(User user);
	public User registerUser(RegisterUser registerUser);
	public User updateUser(User user, String username);
	
	//TODO: delete
	public List<Room> getRooms();
	
	public Page<Room> getRoomPage(RoomFilter roomFilter, Integer page, Integer size);
	public Room getRoomByName(String roomName);
	
	public Room createRoom(Room room);
	public Room updateRoomName(String roomName, String newRoomName);
	public Room updateRoomDescription(String roomName, String newDescription);
	public Room closeRoom(String roomName);
	public Room reopenRoom(String roomName);
	
	public List<Message> getMessagesByRoom(String roomName);
	public Page<Message> getMessagesByRoomPage(String roomName, Integer page, Integer size);
	public Message postMessage(Message message, String roomName);
	public Message updateMessage(Integer id, String newMessage);
	public Integer deleteMessage(Integer id);
	
	public UserMessage likeMessage(UserMessage userMessage, String messageIdStr);
	
	public Category createCategory(Category category);
	
	public RoomCategory addCategoryToRoom(RoomCategory roomCategory, String roomName);
	public Integer removeCategoryFromRoom(RoomCategory roomCategory, String roomName);
	
}
