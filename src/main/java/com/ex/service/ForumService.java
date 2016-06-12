package com.ex.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ex.domain.Category;
import com.ex.domain.Message;
import com.ex.domain.Room;
import com.ex.domain.User;
import com.ex.domain.m2m.RoomCategory;
import com.ex.domain.m2m.UserMessage;

public interface ForumService {
	
	public User getUserDetails(User user);
	public User registerUser(User user);
	public User updateUser(User user);
	
	public List<Room> getRooms();
	public void createRoom(Room room);
	public void closeRoom(String roomName);
	
	public List<Message> getMessagesByRoom(String roomName);
	public Page<Message> getMessagesByRoomPage(String roomName, Integer page, Integer size);
	public void postMessage(Message message, String roomName);
	
	public UserMessage likeMessage(UserMessage userMessage, String messageIdStr);
	
	public Category createCategory(Category category);
	public RoomCategory addCategoryToRoom(RoomCategory roomCategory);
}
