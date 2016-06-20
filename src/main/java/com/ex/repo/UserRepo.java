package com.ex.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ex.domain.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	
	List<User> findByUsername(String username);
	User findOneByUsername(String username);
	List<User> findByUsernameIgnoreCaseContaining(String username);
	
	//TODO: change password
	
}
