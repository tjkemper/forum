package com.ex.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ex.domain.Category;
import com.ex.domain.Message;
import com.ex.domain.Room;
import com.ex.domain.User;
import com.ex.domain.m2m.RoomCategory;
import com.ex.domain.m2m.UserMessage;
import com.ex.model.RegisterUser;
import com.ex.model.RoomFilter;
import com.ex.repo.CategoryRepo;
import com.ex.repo.MessageRepo;
import com.ex.repo.RoomRepo;
import com.ex.repo.UserRepo;
import com.ex.repo.m2m.RoomCategoryRepo;
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
	private CategoryRepo categoryRepo;
	
	@Autowired
	private UserMessageRepo userMessageRepo;
	
	@Autowired
	private RoomCategoryRepo roomCategoryRepo;

	
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
	public User registerUser(RegisterUser registerUser) {
		User user = new User(registerUser.getUsername(), registerUser.getPassword());
		return userRepo.save(user);
	}

	@Override
	public User updateUser(User user, String username) {
		
		User foundUser = userRepo.findOneByUsername(username);
		if(foundUser != null){
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
	public Page<Room> getRoomPage(RoomFilter roomFilter, Integer page, Integer size) {
		
		Pageable pageable = new PageRequest(page, size);
		
		return roomRepo.findAll(pageable);
	}
	
	public Room getRoomByName(String roomName){
		return roomRepo.findOneByName(roomName);
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
	public void updateRoomName(String roomName, String newRoomName){
		
		if(StringUtils.hasText(newRoomName)) {
			Room room = roomRepo.findOneByName(roomName);
			if(room != null){
				room.setName(newRoomName);
			}
		}
	}
	
	@Override
	public void updateRoomDescription(String roomName, String newDescription) {
		Room room = roomRepo.findOneByName(roomName);
		if(room != null){
			room.setDescription(newDescription);
		}
	}
	
	@Override
	public void closeRoom(String roomName) {
		Timestamp now = new Timestamp(new Date().getTime());
		roomRepo.closeRoom(roomName, now);
	}

	@Override
	public void reopenRoom(String roomName) {
		Room room = roomRepo.findOneByName(roomName);
		if(room != null){
			room.setClosed(null);
		}
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
	
	private void enhanceMessageList(Page<Message> messageList){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		System.out.println("LOGGED IN AS:" + name);
		
		Iterator<Message> messageIter = messageList.iterator();
		while(messageIter.hasNext()){
			Message message = messageIter.next();
			Iterator<UserMessage> userMessageIter = message.getUserMessage().iterator();
			while(userMessageIter.hasNext()){
				UserMessage userMessage = userMessageIter.next();
				
				//FIXME: null check on userLikesMessage Boolean
				//calculate numLikes per message
				if(userMessage.getUserLikesMessage()){
					message.setNumLikes(message.getNumLikes() + 1); //add 1 like
				}
				
				//check if authenticated user has a UserMessage relationship
				if(name.equals(userMessage.getUser().getUsername())){
					message.setAuthUserMessage(userMessage);
				}
				
				
			}
		}
		
	}
	
	@Override
	public Page<Message> getMessagesByRoomPage(String roomName, Integer page, Integer size) {
		List<Room> roomList = roomRepo.findByName(roomName);
		if (roomList.size() == 1) {
			Room room = roomList.get(0);
			Pageable pageable = new PageRequest(page, size);
			
			Page<Message> messageList = messageRepo.findByRoomOrderByCreatedDesc(room, pageable);
			enhanceMessageList(messageList);
			return messageList;
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
	public void updateMessage(Integer id, String newMessage) {
		if(id != null && StringUtils.hasText(newMessage)) {
			Message message = messageRepo.findOne(id);
			if(message != null){
				message.setMessage(newMessage);
			}
		}
	}

	@Override
	public void deleteMessage(Integer id) {
		if(id != null){
			messageRepo.delete(id);
		}
	}

	@Override
	public UserMessage likeMessage(UserMessage userMessage, String messageIdStr) {

		String username = userMessage.getUser().getUsername();
		Integer messageId = Integer.parseInt(messageIdStr);
		
		List<UserMessage> userMessageList = userMessageRepo.findByUserUsernameAndMessageId(username, messageId);
		if(userMessageList.size() > 0){
			//User Message relationship already exists
			if(userMessageList.size() == 1){

				UserMessage existingUserMessage = userMessageList.get(0);
				existingUserMessage.setUserLikesMessage(userMessage.getUserLikesMessage());
				return existingUserMessage;
				
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


	@Override
	public Category createCategory(Category category) {
		Category searchedForCategory = categoryRepo.findOneByCategoryName(category.getCategoryName());
		if(searchedForCategory != null){
			return searchedForCategory;
		}else {
			return categoryRepo.save(category);
		}
	}


	@Override
	public RoomCategory addCategoryToRoom(RoomCategory roomCategory, String roomName) {
		
		String categoryName = roomCategory.getCategory().getCategoryName();
		
		RoomCategory existingRoomCategory = roomCategoryRepo.findOneByRoomNameAndCategoryCategoryName(roomName, categoryName);
		
		if(existingRoomCategory == null){
		
			Room room = roomRepo.findOneByName(roomName);
			
			if(room != null){
				
				Category category = createCategory(new Category(categoryName));
				
				roomCategory.setRoom(room);
				roomCategory.setCategory(category);
				return roomCategoryRepo.save(roomCategory);
			}
			
		}else {
			//room already has category
			return null;
		}
		
		
		return null;
	}

	@Override
	public Integer removeCategoryFromRoom(RoomCategory roomCategory, String roomName) {
		return roomCategoryRepo.deleteOneByRoomNameAndCategoryCategoryName(roomName, roomCategory.getCategory().getCategoryName());
	}

}
