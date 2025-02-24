package com.example.weaponMaster;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeaponMasterApplication {

	private static final Dotenv dotenv = Dotenv.load();

	public static void main(String[] args) {
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("NEOPLE_API_KEY", dotenv.get("NEOPLE_API_KEY"));

		SpringApplication.run(WeaponMasterApplication.class, args);
	}
}
