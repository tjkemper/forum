package com.ex.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ex.domain.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	
//	@Query("SELECT id, username, firstName, lastName, email FROM User WHERE username = :usernameVar")
//	List<Object[]> findByUsernameProjected(@Param("usernameVar") String username);

	
	List<User> findByUsername(String username);
	User findOneByUsername(String username);
	
	
//	List<User> findByUsernameAndPassword(String username, String password);
/*	
	@Modifying
	@Query("update User set email = :emailVar, firstName = :firstNameVar, lastName = :lastNameVar where username = :usernameVar")
	void updateUser(@Param("usernameVar") String username, @Param("emailVar") String email, @Param("firstNameVar") String firstName, @Param("lastNameVar") String lastName);
*/	
	//TODO: change password
	
}
