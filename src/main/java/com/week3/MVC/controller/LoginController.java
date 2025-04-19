package com.week3.MVC.controller;

import com.week3.MVC.entities.User;
import com.week3.MVC.repository.ContactUsRepository;
import com.week3.MVC.service.interfaces.EmailService;
import com.week3.MVC.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import javax.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSession;


@Data
@Controller
@Slf4j
public class LoginController {

	@Autowired
	private ContactUsRepository contactUsRepository;
	@Autowired
	EmailService emailService;
	@Autowired
	private UserService userService;

	@Autowired
	private CourseController courseController;

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Sign Up");
		model.addAttribute("user", new User());
		return "sign-up";
	}
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("title", "Login");
		model.addAttribute("user", new User());
		return "login";
	}

	@RequestMapping(value = "/do_login", method = RequestMethod.POST)
	public String loginUser(HttpServletRequest request, @ModelAttribute("user") User user, Model model) {
		log.info("USER : " + user);
		boolean isValidUser = userService.isValidUser(user);

		if (isValidUser) {
			log.info("Session User :  " + user);

			// Add user to the model (optional for the current request/response cycle)
			model.addAttribute("user", user);

			// Get the session and set the user and name attributes
			HttpSession session = request.getSession(); // No casting needed
			session.setAttribute("user", user); // Set the user object in the session
			session.setAttribute("name", user.getName()); // Set the user name in the session

			// Send login success email
//			emailService.sendEmail(user.getEmail(),
//					"Login Successful",
//					"Dear " + user.getName() + ",\n\n"
//							+ "Congratulations! You have successfully logged in to Learning Kart.\n\n"
//							+ "Thank you for choosing Learning Kart for your learning needs.\n\n"
//							+ "Best regards,\n"
//							+ "The Learning Kart Team");

			// Log welcome message
			log.info("Welcome " + user.getName() + "!");
			return "index"; // Redirect to the index page or another page after login
		}

		// If the user is invalid, return to the login page with an error message
		log.info("Invalid email/password.");
		model.addAttribute("error", "Invalid email/password.");
		return "login"; // Return to login page with error message
	}


	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request, Model model) {
		HttpSession session = (HttpSession) request.getSession(false); // Get the current session, false means it won't create a new one if it doesn't exist.

		if (session != null) {
			session.invalidate(); // Invalidate the session
		}

		model.addAttribute("success", "Logged out successfully.");
		log.info("Logged out successfully.");

		return "index"; // Redirect to index or your home page after logging out
	}


}
