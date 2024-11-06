package com.Ecommerce.store;

import com.Ecommerce.store.Security.JwtHelper;
import com.Ecommerce.store.entities.User;
import com.Ecommerce.store.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SastaSafarApplicationTests {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private JwtHelper jwtHelper;

	@Test
	void contextLoads() {
	}

	@Test
	void testToken(){
		User user = userRepo.findByEmail("yash32860@gmail.com").get();
		String s = jwtHelper.generateToken(String.valueOf(user));
		System.out.println(s);

		System.out.println(jwtHelper.getUsernameFromToken(s));
		System.out.println(jwtHelper.isTokenExpired(s));
	}

}
