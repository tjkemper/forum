package com.ex.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ex.domain.Message;
import com.ex.domain.Room;
import com.ex.domain.User;
import com.ex.domain.m2m.UserMessage;
import com.ex.repo.MessageRepo;
import com.ex.repo.RoomRepo;
import com.ex.repo.UserRepo;
import com.ex.repo.m2m.UserMessageRepo;

@Service
@Transactional
public class ForumServiceImpl implements ForumService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RoomRepo roomRepo;
	
	@Autowired
	private MessageRepo messageRepo;
	
	@Autowired
	private UserMessageRepo userMessageRepo;

	
	@Override
	public User getUserDetails(User user) {
		List<User> userList = userRepo.findByUsername(user.getUsername());
		
		if (userList.size() == 1) {
			return userList.get(0);
		}else {
			return null;
		}
	}

	
	@Override
	public User registerUser(User user) {
		return userRepo.save(user);
	}

	@Override
	public User updateUser(User user) {
//		userRepo.updateUser(user.getUsername(), user.getEmail(), user.getFirstName(), user.getPassword());
//		return userRepo.findByUsername(user.getUsername());
		
		List<User> userList = userRepo.findByUsername(user.getUsername());
		if(userList.size() == 1){
			User foundUser = userList.get(0);
			foundUser.setEmail(user.getEmail());
			foundUser.setFirstName(user.getFirstName());
			foundUser.setLastName(user.getLastName());
			return foundUser;
		}else {
			return null;
		}
		
	}
	
	@Override
	public List<Room> getRooms() {
		return roomRepo.findAll();
	}
	
	@Override
	public void createRoom(Room room) {
		
		List<User> userList = userRepo.findByUsername(room.getOwner().getUsername());
		if(userList.size() == 1){
			User owner = userList.get(0);
			
			Timestamp now = new Timestamp(new Date().getTime());

			Room newRoom = new Room(room.getName(), room.getDescription(), owner, now);
			System.out.println("createRoom success");
			System.out.println(newRoom);
			roomRepo.save(newRoom);
		}
		
	}
	
	@Override
	public void closeRoom(String roomName) {
		Timestamp now = new Timestamp(new Date().getTime());
		roomRepo.closeRoom(roomName, now);
	}


	@Override
	public List<Message> getMessagesByRoom(String roomName) {
		List<Room> roomList = roomRepo.findByName(roomName);
		if (roomList.size() == 1) {
			Room room = roomList.get(0);
			return messageRepo.findByRoomOrderByCreatedDesc(room);
		} else {
			return null;
		}
	}

	@Override
	public Page<Message> getMessagesByRoomPage(String roomName, Integer page, Integer size) {
		List<Room> roomList = roomRepo.findByName(roomName);
		if (roomList.size() == 1) {
			Room room = roomList.get(0);
			Pageable pageable = new PageRequest(page, size);
			return messageRepo.findByRoomOrderByCreatedDesc(room, pageable);
		} else {
			return null;
		}
	}
	
	@Override
	public void postMessage(Message message, String roomName) {

		List<Room> roomList = roomRepo.findByName(roomName);
		List<User> userList = userRepo.findByUsername(message.getOwner().getUsername());
		if (roomList.size() == 1 && userList.size() == 1) {
			User owner = userList.get(0);
			Timestamp now = new Timestamp(new Date().getTime());
			Message newMessage = new Message(message.getMessage(), owner, now, roomList.get(0));
			messageRepo.save(newMessage);
		}
	}


	@Override
	public UserMessage likeMessage(UserMessage userMessage) {

		String username = userMessage.getUser().getUsername();
		Integer messageId = userMessage.getMessage().getId();
		
		List<UserMessage> userMessageList = userMessageRepo.findByUserUsernameAndMessageId(username, messageId);
		if(userMessageList.size() > 0){
			//User Message relationship already exists
			if(userMessageList.size() == 1){

				UserMessage existingUserMessage = userMessageList.get(0);
				existingUserMessage.setUserLikesMessage(userMessage.getUserLikesMessage());
				
			}else {
				//FIXME: Multiple User Message relationships (should not get here)
			}
		}else {
			//User Message relationship does not exist
			if(username != null && messageId != null){
				List<User> userList = userRepo.findByUsername(username);
				
				if(userList.size() == 1){
					User user = userList.get(0);
				
					Message message = messageRepo.findOne(messageId);
					if(user != null && message != null){
						userMessage.setUser(user);
						userMessage.setMessage(message);
						return userMessageRepo.save(userMessage);
					}
				}
				
			}
		}
			
		return null;
	}


}
