package com.userFront.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.userFront.domain.User;

public interface UserDao extends CrudRepository<User, Long> {
	// @Query(value = "SELECT * FROM user WHERE username =:username", nativeQuery = true)
	User findByUsername(String username);
	User findByEmail(String email);
	User findByPanNumber(String panNumber);
	User findByAadharNumber(String aadharNumber);
    
	List<User> findAll();
	
	@Query("Select user from User user where user.userId IN (SELECT userRoleId FROM UserRole role where role_id=0) and user.kycStatus=?")	
	List<User> findBySpecificRoles(String ud);
	
}
