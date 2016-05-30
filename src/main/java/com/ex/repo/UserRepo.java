package com.ex.repo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ex.domain.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	List<User> findByUsername(String username);
	List<User> findByUsernameAndPassword(String username, String password);
/*	
	@Modifying
	@Query("update User set email = :emailVar, firstName = :firstNameVar, lastName = :lastNameVar where username = :usernameVar")
	void updateUser(@Param("usernameVar") String username, @Param("emailVar") String email, @Param("firstNameVar") String firstName, @Param("lastNameVar") String lastName);
*/	
	//TODO: change password
	
}
