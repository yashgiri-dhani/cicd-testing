package com.example.hospitalManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HospitalManagementApplication {

	public static void main(String[] args) {

        SpringApplication.run(HospitalManagementApplication.class, args);
        System.out.println("The server has started");
	}

}
