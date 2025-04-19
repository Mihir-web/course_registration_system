package com.week3.MVC.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;


@Data
@NoArgsConstructor
@Entity
@Lazy(value = false)
@Table(name="COURSE")
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id",
		scope= Course.class)
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@Column(name = "courseName")
	private String courseName;

	@Column(name = "courseDescription")
	private String courseDescription;

	@Column(name = "duration")
	private Integer duration;

	@Column(name = "price")
	private Integer price = 0;

	@Column(name = "rating")
	private Double rating = 4.5;

	@Column(name = "trainer")
	private String trainer;

	@Transient
	private boolean isEnrolled;

	@Transient
	private String imageId;

}
