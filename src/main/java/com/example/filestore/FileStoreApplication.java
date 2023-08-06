package com.example.filestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.yml")
public class FileStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileStoreApplication.class, args);
	}

}
