package com.week3.MVC.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.annotation.Lazy;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ToString
@Getter
@NoArgsConstructor
@Data
@Entity
@Lazy(value = false)
@Table(name = "trainer")
public class Trainer {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id")
        private Integer id;

        @Column(name = "name")
        @Size(min = 10, max = 200, message = "Name must be between 10 and 200 characters")
        private String name;
        @NotNull(message = "Email is Mandatory.")
        @Email(message = "Email should be valid")
        @Column(name = "email")
        private String email;

        @Column(name = "rating")
        private String rating;
}
