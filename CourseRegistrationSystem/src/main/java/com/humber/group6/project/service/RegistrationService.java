package com.humber.group6.project.service;


import com.humber.group6.project.entity.Registration;
import com.humber.group6.project.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegistrationService {
    @Autowired
    private RegistrationRepository registrationRepository;

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public void registerStudent(Registration registration) {
        registrationRepository.save(registration);
    }
}
