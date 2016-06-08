package com.ex.repo.m2m;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.domain.User;
import com.ex.domain.m2m.UserMessage;

public interface UserMessageRepo extends JpaRepository<UserMessage, Integer> {
	
	public List<UserMessage> findByUserUsernameAndMessageId(String username, Integer id);
	
}
