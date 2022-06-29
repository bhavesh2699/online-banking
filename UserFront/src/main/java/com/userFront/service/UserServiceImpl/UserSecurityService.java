package com.userFront.service.UserServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.userFront.dao.UserDao;
import com.userFront.domain.User;

@Service
public class UserSecurityService implements UserDetailsService {

	/** The application logger */
	private static final Logger LOG = LoggerFactory.getLogger(UserSecurityService.class);

	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if (user == null) {
			LOG.warn("Username {} not found", username);
			throw new UsernameNotFoundException("Username " + username + " not found");
		}
		 else if(user.getKycStatus().equals("Pending") && user.hasRole("ROLE_USER")) {
			 LOG.warn("Username {} KYC pending", username);
				throw new UsernameNotFoundException("Username " + username + " KYC pending");
		}
		 else if(user.getKycStatus().equals("Rejected") && user.hasRole("ROLE_USER")) {
			 LOG.warn("Username {} KYC rejected", username);
				throw new UsernameNotFoundException("Username " + username + " KYC rejected");
		}
		return user;
	}
}
