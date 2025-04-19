package com.week3.MVC;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAutoConfiguration
@EnableAsync
@EnableJpaRepositories(basePackages = { "com.week3.MVC.repository"})
@ComponentScan(basePackages = { "com.week3.MVC.*" })
@EntityScan(basePackages = { "com.week3.MVC.entities" })
@Slf4j
public class MvcApplication {

	public static void main(String[] args) {
		try {
			log.info("Application Starting...");
			SpringApplication.run(MvcApplication.class, args);
			log.info("Application Started Successfully.");
		} catch (Exception ex) {
			log.error("Failed to start application" + ex);
		}
	}

}
