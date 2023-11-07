package com.surf.editor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EditorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EditorApplication.class, args);
	}

}
