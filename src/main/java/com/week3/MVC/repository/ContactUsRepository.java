package com.week3.MVC.repository;

import com.week3.MVC.entities.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ContactUsRepository extends JpaRepository<ContactUs, Integer> {
}