package com.week3.MVC.controller;

import com.week3.MVC.entities.User;
import com.week3.MVC.service.interfaces.EmailService;
import com.week3.MVC.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    EmailService emailService;
    @Autowired
    private CourseController courseController;

    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(HttpServletRequest  session, @ModelAttribute("user") User user, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model) {
//        if (!agreement) {
//            log.info("You have not agreed the terms and conditions.");
//            model.addAttribute("error", "You have not agreed the terms and conditions.");
//            return "sign-up";
//        }
        log.info("USER " + user);
        boolean isDuplicateUser = userService.isUserAlreadyExists(user.getEmail());

        if(isDuplicateUser){
            model.addAttribute("error", "User already exists in database.");
            log.info("User already exists in database.");
            return "sign-up";
        }
        User dbUser = userService.save(user);
        session.setAttribute("user", dbUser);
        model.addAttribute("user", dbUser);
        session.setAttribute("name", dbUser.getName());
        emailService.sendEmail(dbUser.getEmail(),
                "Registration Successful",
                "Dear "+user.getName()+","+"\n\n"
                        + "Congratulations! You have successfully Registered to Learning Kart.\n\n"
                        + "Thank you for choosing Learning Kart for your learning needs.\n\n"
                        + "Best regards,\n"
                        + "The Learning Kart Team ");
//        model.addAttribute("info", "Welcome "+ dbUser.getName() + "!");
//        model.addAttribute("success", "User registered successfully.");
        log.info("User "+ dbUser.getName() + " successfully Registered.");
        return "index";
    }

    @GetMapping(value = "/profile")
    public String profile(HttpServletRequest  session, Model model) {
        model.addAttribute("title", "Profile");
        User sessionUser = (User) session.getAttribute("user");
        model.addAttribute("user", sessionUser);
       return "profile";
    }

    @RequestMapping(value = "/update-profile", method = RequestMethod.POST)
    public String updateUser(HttpServletRequest  session, @ModelAttribute("user") User user, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        log.info("USER : " + user);
        log.info("sessionUser : " + user);
        if(StringUtils.equalsIgnoreCase(sessionUser.getEmail(),user.getEmail())){
            user.setId(sessionUser.getId());
            if(StringUtils.isEmpty(user.getPassword())){
                user.setPassword(sessionUser.getPassword());
            }
            userService.save(user);
            session.setAttribute("user", user);
            session.setAttribute("name", user.getName());
            model.addAttribute("success", "Profile updated successfully.");
            log.info("Profile updated successfully.");
            return "index";
        }
        model.addAttribute("error", "Email update not allowed.");
        log.info("Email update not allowed.");
        return "profile";
    }

    @GetMapping(value = "/delete-profile")
    public String deleteUser(HttpServletRequest request, Model model) {
        // Retrieve the session
        HttpSession session = (HttpSession) request.getSession(false); // false means it won't create a new session if one doesn't exist

        // Check if the session exists and if the user is logged in
        if (session != null) {
            User sessionUser = (User) session.getAttribute("user");

            if (sessionUser != null) {
                log.info("Session User: " + sessionUser);
                userService.deleteById(sessionUser.getId());
                session.invalidate(); // Invalidate the session to log the user out
                model.addAttribute("success", "Profile deleted successfully.");
                log.info("Profile deleted successfully.");
            } else {
                model.addAttribute("error", "User not found in session.");
            }
        } else {
            model.addAttribute("error", "No active session found.");
        }

        return "index";
    }


}
