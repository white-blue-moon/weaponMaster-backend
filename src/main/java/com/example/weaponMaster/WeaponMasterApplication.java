package com.example.weaponMaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WeaponMasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeaponMasterApplication.class, args);
	}
}
