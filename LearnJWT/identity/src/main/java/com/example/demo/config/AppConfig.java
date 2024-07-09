package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppConfig {
	PasswordEncoder passwordEncoder;
	
	@Bean
	ApplicationRunner applicationRunner(UserRepo userRepo) {
		return args -> {
			if (userRepo.findByUsername("admin").isEmpty()) {
				Set<String> roles = new HashSet<>();
//				roles.add(Role.ADMIN.name());
				User user = User.builder()
						.username("admin")
						.password(passwordEncoder.encode("admin"))
//						.roles(roles)
						.build();
				userRepo.save(user);
				log.warn("admin user has been created with default password: admin, please change it");
			}
		};
	}
}
