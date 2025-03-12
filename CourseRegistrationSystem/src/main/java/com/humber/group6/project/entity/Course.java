package com.humber.group6.project.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int availableSeats;

    @OneToMany(mappedBy = "course")
    private List<Registration> registrations;

    // Getters and Setters
}
