package com.userFront.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userFront.domain.User;
import com.userFront.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    
	
	@Autowired
	private UserService userService;
	 //profile controller logic

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(Principal principal, Model model) {
		
		User user = userService.findByUsername(principal.getName());
		
		User user1 = userService.findByUsername(principal.getName());
		user1.setPassportSizePhoto("data:image/jpeg;base64,"+user.getPassportSizePhoto());
		user1.setPanPhoto("data:image/jpeg;base64,"+user.getPanPhoto());
		user1.setAadharPhoto("data:image/jpeg;base64,"+user.getAadharPhoto());
		model.addAttribute("profilepage", user);
		model.addAttribute("profilepage1", user1);

		return "profile";
	}

}
