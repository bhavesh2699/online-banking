package com.userFront.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.userFront.dao.RoleDao;
import com.userFront.dao.UserDao;
import com.userFront.domain.SavingsAccount;
import com.userFront.domain.User;
import com.userFront.domain.security.UserRole;
import com.userFront.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserDao userDao;

	@RequestMapping("/")
	public String home() {
		return "redirect:/index";
	}

	@GetMapping("/admin")
	public String admin(Principal principal, Model model) {
		List<User> user1 = userDao.findBySpecificRoles("Pending");
		model.addAttribute("user2", user1);
		return "pending";

	}

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		User user = new User();

		model.addAttribute("user", user);

		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signupPost(@RequestParam("username") String name, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("phone") String phone,
			@RequestParam("email") String email, @RequestParam("permanentAddress") String permanentAddress,
			@RequestParam("currentAddress") String currentAddress, @RequestParam("aadharNumber") String aadharNumber,
			@RequestParam("panNumber") String panNumber, @RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("passportSizePhoto") MultipartFile passportSizePhoto,
			@RequestParam("panPhoto") MultipartFile panPhoto, @RequestParam("aadharPhoto") MultipartFile aadharPhoto,
			Model model) throws IOException {

		User user = new User(username, password, firstName, lastName, email, phone, permanentAddress, currentAddress,
				aadharNumber, panNumber, Base64.getEncoder().encodeToString(passportSizePhoto.getBytes()),
				Base64.getEncoder().encodeToString(aadharPhoto.getBytes()),
				Base64.getEncoder().encodeToString(panPhoto.getBytes()));

		if (userService.checkUserExists(user.getUsername(), user.getEmail(), user.getPanNumber(),
				user.getAadharNumber())) {
			if (userService.checkEmailExists(user.getEmail())) {
				model.addAttribute("emailExists", true);
			}

			if (userService.checkUsernameExists(user.getUsername())) {
				model.addAttribute("usernameExists", true);
			}

			if (userService.checkPanNumberExists(user.getPanNumber())) {
				model.addAttribute("panNumberExists", true);
			}

			if (userService.checkAadharNumberExists(user.getAadharNumber())) {
				model.addAttribute("aadharNumberExists", true);
			}
			model.addAttribute("user", user);
			return "signup";
		} else {
			Set<UserRole> userRoles = new HashSet<>();
			userRoles.add(new UserRole(user, roleDao.findByName("ROLE_USER")));

			userService.createUser(user, userRoles);

			return "redirect:/";
		}
	}

	@GetMapping("/index-error")
	public String login(@RequestParam String username, Model model) {
		User user = userService.findByUsername(username);
		if (user == null)
			model.addAttribute("errorMessage", true);
		else if (user.getKycStatus().equals("Pending")) {
			model.addAttribute("pending", true);
		} else if (user.getKycStatus().equals("Rejected")) {
			model.addAttribute("rejected", true);
		}

		return "index";
	}

	// userdashboard controller
	@RequestMapping("/userFront")
	public String userFront(Principal principal, Model model) {

		User user = userService.findByUsername(principal.getName());
		SavingsAccount savingsAccount = user.getSavingsAccount();
		System.out.println(savingsAccount);

		model.addAttribute("savingsAccount", savingsAccount);
		return "userFront";
	}

	// Confirmed user
	@RequestMapping("/Confirmed")
	public String pending(Model model) {

		List<User> user1 = userDao.findBySpecificRoles("Confirmed");
		model.addAttribute("user2", user1);

		return "Confirmed";
	}

	// pending user
	@RequestMapping("/Pending")
	public String confirmedpe(Model model) {
		List<User> user1 = userDao.findBySpecificRoles("Pending");
		model.addAttribute("user2", user1);
		return "pending";
	}

	// Rejected user
	@GetMapping("/Rejected")
	public String confirmq(Model model) {
		List<User> user1 = userDao.findBySpecificRoles("Rejected");

		model.addAttribute("user2", user1);
		return "Confirmed";
	}

	// confirm single user
	@GetMapping("/Confirm/single/{username}")
	public String con(@ModelAttribute("user") User user, Model model, @PathVariable String username) {
		User user1 = userService.findByUsername(username);
		user1.setKycStatus("Confirmed");
		userService.updatestudent(user1);

		return "redirect:/Confirmed";

	}

	// reject single user
	@GetMapping("/Reject/single/{username}")
	public String rej(@ModelAttribute("user") User user, Model model, @PathVariable String username) {
		User user1 = userService.findByUsername(username);
		user1.setKycStatus("Rejected");
		userService.updatestudent(user1);

		return "redirect:/Rejected";

	}

}
