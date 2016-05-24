package com.ex.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.domain.Message;
import com.ex.domain.Room;

public interface MessageRepo extends JpaRepository<Message, Integer> {
	List<Message> findByRoom(Room room);
	List<Message> findByRoomOrderByCreatedDesc(Room room);
	Page<Message> findByRoomOrderByCreatedDesc(Room room, Pageable pageable);
}
