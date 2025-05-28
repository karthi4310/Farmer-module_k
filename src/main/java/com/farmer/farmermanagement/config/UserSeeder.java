// package com.farmer.farmermanagement.config;

// import java.time.LocalDate;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;

// import com.farmer.farmermanagement.entity.User;
// import com.farmer.farmermanagement.repository.UserRepository;
// import com.farmer.farmermanagement.utils.EmailService;

// @Component
// public class UserSeeder implements CommandLineRunner {

// 	private final UserRepository userRepository;
// 	private final PasswordEncoder passwordEncoder;
//     private final EmailService  emailService;

// 	public UserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService  emailService) {
// 		this.userRepository = userRepository;
// 		this.passwordEncoder = passwordEncoder;
//         this.emailService = emailService;
// 	}

// 	@Override
// 	public void run(String... args) throws Exception {
// 		emailService.sendOtpEmail("karthik.m@hinfinity.in", "1234");
// 	}
// }
