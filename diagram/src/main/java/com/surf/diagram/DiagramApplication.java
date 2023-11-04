package com.surf.diagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DiagramApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiagramApplication.class, args);
	}

}
