package com.humber.group6.project.controller;



import com.humber.group6.project.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/registrations")
    public String listRegistrations(Model model) {
        model.addAttribute("registrations", registrationService.getAllRegistrations());
        return "registration";
    }
}
