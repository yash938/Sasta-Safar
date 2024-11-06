package com.Ecommerce.store;

import com.Ecommerce.store.entities.Role;
import com.Ecommerce.store.entities.User;
import com.Ecommerce.store.repository.RoleRepo;
import com.Ecommerce.store.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SastaSafarApplication implements CommandLineRunner {
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SastaSafarApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {


		Role admin = roleRepo.findByName("ROLE_ADMIN").orElse(null);
		Role normal = roleRepo.findByName("ROLE_NORMAL").orElse(null);

		// Check and create ROLE_ADMIN if it doesn't exist
		if (admin == null) {
			admin = new Role();
			admin.setName("ROLE_ADMIN");
			roleRepo.save(admin);
		}

		// Check and create ROLE_NORMAL if it doesn't exist
		if (normal == null) {
			normal = new Role();
			normal.setName("ROLE_NORMAL");
			roleRepo.save(normal);
		}

		// Find the user by email and create if not found
		User user = userRepo.findByEmail("yash32860@gmail.com").orElse(null);
		if (user == null) {
			user = new User();
			user.setName("yash");
			user.setEmail("yash32860@gmail.com");
			user.setGender("male");
			user.setPassword(passwordEncoder.encode("sharma"));
			user.setAbout("I am a java developer");

			// Setting roles for the user
			List<Role> roles = new ArrayList<>();
			roles.add(admin); // Add admin role
			roles.add(normal); // You can also add normal role if needed
			user.setRoles(List.of(admin));

			userRepo.save(user);
			System.out.println("User is created successfully");
		} else {
			System.out.println("User already exists");
		}
	}

}
