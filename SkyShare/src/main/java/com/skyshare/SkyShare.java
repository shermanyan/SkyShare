package com.skyshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.skyshare")
public class SkyShare {

	public static void main(String[] args) {
		SpringApplication.run(SkyShare.class, args);
	}

}
