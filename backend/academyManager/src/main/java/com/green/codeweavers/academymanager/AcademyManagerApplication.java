package com.green.codeweavers.academymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class AcademyManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(AcademyManagerApplication.class, args);
	}
}

