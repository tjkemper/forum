package com.ex.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.domain.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	List<User> findByUsername(String username);
	List<User> findByUsernameAndPassword(String username, String password);
	
}
