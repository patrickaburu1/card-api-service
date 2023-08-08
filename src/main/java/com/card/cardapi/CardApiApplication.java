package com.card.cardapi;

import com.card.cardapi.services.UserService;
import com.card.cardapi.entities.Role;
import com.card.cardapi.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@AllArgsConstructor

public class CardApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardApiApplication.class, args);
	}



	@Bean
	CommandLineRunner run(UserService userService){

		return args -> {
//			userService.saveRole(new Role(1L,"ROLE_MEMBER"));
//			userService.saveRole(new Role(2L,"ROLE_ADMIN"));
//			userService.saveRole(new Role(3L,"ROLE_SUPER_ADMIN"));
//			userService.saveRole(new Role(4L,"ROLE_ADMIN_MANAGER"));
//
//
//			userService.saveUser(new User(1L,"Nesh ","nesh@gmail.com","1234",new ArrayList<>()));
//			userService.saveUser(new User(2L,"Nelly Nel","nelly@gmail.com","1234",new ArrayList<>()));
//			userService.saveUser(new User(3L,"patrick kabz","patrick@gmail.com","1234",new ArrayList<>()));
//
//
//			userService.addRoleToUser("nesh@gmail.com", "ROLE_ADMIN");
//			userService.addRoleToUser("patrick@gmail.com", "ROLE_MEMBER");
//			userService.addRoleToUser("nelly@gmail.com", "ROLE_MEMBER");

		};
	}



	@Bean
	BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}


}
