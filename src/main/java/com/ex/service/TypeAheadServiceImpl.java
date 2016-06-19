package com.ex.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ex.domain.Room;
import com.ex.repo.RoomRepo;

@Service
@Transactional
public class TypeAheadServiceImpl implements TypeAheadService {

	@Autowired
	private RoomRepo roomRepo;
	
	
	@Override
	public List<Room> getRoomsWithRoomNameLike(String roomName) {
		return roomRepo.findByNameIgnoreCaseContaining(roomName);
	}

	@Override
	public List<String> getUsernamesLike(String username) {
		return null;
	}

	@Override
	public List<String> getCategoriesLike(String category) {
		return null;
	}
	
	
}
