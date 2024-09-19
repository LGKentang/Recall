package com.example.recall;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.recall.firebase.FirebaseInitializer;

@SpringBootApplication
public class Main {

	public static void main(String[] args) throws IOException {
		FirebaseInitializer.init();
		SpringApplication.run(Main.class, args);
	}

}
