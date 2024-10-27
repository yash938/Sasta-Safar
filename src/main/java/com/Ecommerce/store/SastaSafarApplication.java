package com.Ecommerce.store;

import com.Ecommerce.store.entities.Role;
import com.Ecommerce.store.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SastaSafarApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SastaSafarApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${role.normal.id}")
	private int normal_role_id;

	@Value("${role.admin.id}")
	private int admin_role_id;


	@Autowired
	private RoleRepo roleRepo;
	@Override
	public void run(String... args) throws Exception {
//try{
//	Role roleAdmin = Role.builder().roleId(admin_role_id).roleName("ROLE_ADMIN").build();
//	Role roleNormal = Role.builder().roleId(normal_role_id).roleName("ROLE_NORMAL").build();
//	roleRepo.save(roleNormal);
//	roleRepo.save(roleAdmin);
//}catch (Exception e){
//	e.printStackTrace();
//}

	}
}
