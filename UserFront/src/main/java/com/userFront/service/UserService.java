package com.userFront.service;

import java.util.List;
import java.util.Set;

import com.userFront.domain.User;
import com.userFront.domain.security.UserRole;

public interface UserService {
	
	User findByUsername(String username);
	User findByEmail(String email);

	boolean checkUserExists(String username, String email, String panNumber, String aadharNumber);
	boolean checkUsernameExists(String username);
	boolean checkEmailExists(String email);
	boolean checkPanNumberExists(String panNumber);
	boolean checkAadharNumberExists(String aadharNumber);
	
	void save(User user);
	User createUser(User user, Set<UserRole> userRoles);
	User saveUser(User user);
	
	List<User> findUserList();
	
    void enableUser (String username);

    void disableUser (String username);
	User updatestudent(User user1);
}
