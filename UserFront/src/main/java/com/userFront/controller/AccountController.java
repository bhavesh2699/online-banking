package com.userFront.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.userFront.domain.SavingsAccount;
import com.userFront.domain.User;
import com.userFront.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/savingsAccount")
	public String savingsAccount(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		SavingsAccount savingsAccount = user.getSavingsAccount();

		model.addAttribute("savingsAccount", savingsAccount);

		return "savingsAccount";
	}
	
}
