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



}
