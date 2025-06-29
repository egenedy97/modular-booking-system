package com.example.modular_booking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ModularBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModularBookingSystemApplication.class, args);
	}

}
