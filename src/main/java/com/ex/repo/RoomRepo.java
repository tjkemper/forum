package com.ex.repo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ex.domain.Room;

public interface RoomRepo extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {
	
	List<Room> findByName(String name);
	Room findOneByName(String name);
	
	@Modifying
	@Query("update Room set closed = :closedVar where name = :roomNameVar")
	void closeRoom(@Param("roomNameVar") String roomName, @Param("closedVar") Timestamp closed);
	
	List<Room> findByNameIgnoreCaseContaining(String name);
}
