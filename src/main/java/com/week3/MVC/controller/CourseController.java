package com.week3.MVC.controller;


import com.week3.MVC.entities.Course;
import com.week3.MVC.entities.CourseEnrollment;
import com.week3.MVC.entities.User;
import com.week3.MVC.repository.CourseEnrollmentRepository;
import com.week3.MVC.service.interfaces.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Data
@Controller
@Slf4j
public class CourseController {

	@Autowired
	private CourseEnrollmentRepository courseEnrollmentRepository;

	@Autowired
	private CourseService courseService;

	@GetMapping("/courses")
	public String courses(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false); // Retrieve existing session if available
		if (session == null || session.getAttribute("user") == null) {
			// If session is null or user attribute is missing, redirect to login page
			model.addAttribute("error", "Please log in to access the courses.");
			return "login"; // Redirect to the login page (change based on your application logic)
		}

		// Get user from session and extract userId
		User sessionUser = (User) session.getAttribute("user");
		Integer userId = sessionUser.getId(); // Assuming the User class has a getId() method

		// Fetch courses based on userId
		List<Course> courseList = courseService.getCourseList(userId);
		log.info("courseList : " + courseList);

		// Add attributes to the model and return the view
		model.addAttribute("title", "Courses");
		model.addAttribute("courseList", courseList);
		return "courses/courses"; // Return to the course list view
	}


	private Integer getUserIdFromSession(HttpServletRequest  session) {
		User sessionUser = (User) session.getAttribute("user");
		return sessionUser != null ? sessionUser.getId() : null;
	}

	@PostMapping("/search")
	public String search(HttpServletRequest  session, @RequestParam(value = "searchText") String searchText, Model model) {
		Integer userId = getUserIdFromSession(session);
		log.info("Search Criteria : " + searchText);
		List<Course> courseList = courseService.search(searchText, userId);
		log.info("courseList : " + courseList);
		model.addAttribute("title", "Courses");
		model.addAttribute("courseList", courseList);
		return "courses/courses";
	}

	@RequestMapping(value = "/enroll/{id}", method = RequestMethod.GET)
	public String enrollUser(HttpSession session, @PathVariable("id") int courseId, Model model) {
		log.info("Course for Enrollment : " +  courseId);
		User sessionUser = (User) session.getAttribute("user");
		CourseEnrollment courseEnrollment = new CourseEnrollment();
		courseEnrollment.setCourseId(courseId);
		courseEnrollment.setUserId(sessionUser.getId());
		courseEnrollment.setUserName(sessionUser.getName());
		CourseEnrollment dbEnrollment = courseEnrollmentRepository.save(courseEnrollment);

		model.addAttribute("success", sessionUser.getName() + " successfully enrolled for courseId : " + courseId);
		log.info("success" +  sessionUser.getName() + " successfully enrolled for courseId : " + courseId);
		return myCourses((HttpServletRequest) session, model);
	}

	@GetMapping(value = "/init-add-course")
	public String initAddCourse(Model model) {
		model.addAttribute("course", new Course());
		model.addAttribute("title", "Add Course");
		log.info("loading init-add-course..!");
		return "courses/add-course";
	}

	@RequestMapping(value = "/submit-add-course", method = RequestMethod.POST)
	public String submitAddCourse(@ModelAttribute("course") Course course, Model model) {
		Course dbCourse = courseService.save(course);
		model.addAttribute("success", dbCourse.getCourseName() + " added successfully. You can continue to add more..!");
		log.info("success - " + course.getCourseName() + " added successfully. Course =  : " + dbCourse);
		return initAddCourse(model);
	}

	@RequestMapping(value = "/init-manage-course", method = RequestMethod.POST)
	public String initManageCourse(@ModelAttribute("course") Course course, Model model) {
		model.addAttribute("course", course);
		model.addAttribute("title", "Manage Course");
		log.info("loading init-manage-course..! course=" + course);
		return "courses/manage-course";
	}

	@RequestMapping(value = "/submit-manage-course", method = RequestMethod.POST)
	public String submitManageCourse(HttpServletRequest  session, @ModelAttribute("course") Course course, Model model) {
		log.info("course=" + course);
		if(course.getId()>0){
			Course dbCourse = courseService.save(course);
			model.addAttribute("success", dbCourse.getCourseName() + " updated successfully.");
			return courses(session, model);
		}
		model.addAttribute("error", course.getCourseName() + " update failed.");
		return courses(session, model);
	}

	@RequestMapping(value = "/delete-course/{id}", method = RequestMethod.GET)
	public String deleteCourse(HttpServletRequest  session, @PathVariable("id") int id, Model model) {
		log.info("Id=" + id);
		if(id>0){
			try {
				courseService.deleteById(id);
				model.addAttribute("success", "Course with " + id + " deleted successfully.");
			} catch (Exception e) {
				log.error("Error : " + e.getLocalizedMessage());
				model.addAttribute("error", "Course with id : " + id + " delete failed with exception.");			}
			return courses(session, model);
		}
		model.addAttribute("error", "Course with id : " + id + " delete failed.");
		return courses(session, model);
	}

	@GetMapping("/my-courses")
	public String myCourses(HttpServletRequest session, Model model) {
		User sessionUser = (User) session.getAttribute("user");
		List<Course> courseList = courseService.getEnrolledCourseList(sessionUser.getId());
		log.info("courseList"+courseList);
		log.info("courseList : " + courseList);
		if(!courseList.isEmpty()) {
			model.addAttribute("title", "My Courses");
			model.addAttribute("courseList", courseList);
			return "courses/courses";
		}
		model.addAttribute("message","Not Enrolled for any courses");
		System.out.println("not enrolled");
		return "courses/courses";
	}

	@RequestMapping(value = "/init-course-detail", method = RequestMethod.POST)
	public String courseDetail(HttpServletRequest session, @ModelAttribute  Course course,  Model model) {
		model.addAttribute("course", course);
		model.addAttribute("title", "Course Detail");
		log.info("loading init-course-detail..! course=" + course);
		return "courses/course-details" ;
	}

}
