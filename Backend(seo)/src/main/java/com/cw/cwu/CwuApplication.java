package com.cw.cwu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.cw.cwu")
public class CwuApplication {

	public static void main(String[] args) {
		SpringApplication.run(CwuApplication.class, args);
	}

}
