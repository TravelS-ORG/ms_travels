package com.group.ms_travels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MsTravelsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTravelsApplication.class, args);
	}

}
