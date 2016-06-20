package com.ex.service;

import java.util.List;

import com.ex.domain.Room;
import com.ex.domain.User;

public interface TypeAheadService {
	
	List<Room> getRoomsWithRoomNameLike(String roomName);
	List<User> getUsersWithUsernameLike(String username);
	List<String> getCategoriesLike(String category);
	
}
