 package com.relesi.modernizationbiometric.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ModernizationBiometricApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModernizationBiometricApplication.class, args);
	}
}
