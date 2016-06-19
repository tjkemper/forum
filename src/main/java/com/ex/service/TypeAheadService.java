package com.ex.service;

import java.util.List;

import com.ex.domain.Room;

public interface TypeAheadService {
	
	List<Room> getRoomsWithRoomNameLike(String roomName);
	List<String> getUsernamesLike(String username);
	List<String> getCategoriesLike(String category);
	
}
