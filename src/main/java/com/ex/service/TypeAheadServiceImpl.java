package com.ex.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ex.domain.Room;
import com.ex.domain.User;
import com.ex.repo.RoomRepo;
import com.ex.repo.UserRepo;

@Service
@Transactional
public class TypeAheadServiceImpl implements TypeAheadService {

	@Autowired
	private RoomRepo roomRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	
	@Override
	public List<Room> getRoomsWithRoomNameLike(String roomName) {
		return roomRepo.findByNameIgnoreCaseContaining(roomName);
	}

	@Override
	public List<User> getUsersWithUsernameLike(String username) {
		return userRepo.findByUsernameIgnoreCaseContaining(username);
	}

	@Override
	public List<String> getCategoriesLike(String category) {
		return null;
	}
	
	
}
