package com.ex.repo.m2m;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.domain.m2m.UserMessage;

public interface UserMessageRepo extends JpaRepository<UserMessage, Integer> {

}
