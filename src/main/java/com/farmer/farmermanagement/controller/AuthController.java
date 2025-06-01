package com.farmer.farmermanagement.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmer.farmermanagement.dto.ForgotPasswordDTO;
import com.farmer.farmermanagement.dto.ForgotUserIdDTO;
import com.farmer.farmermanagement.dto.LoginRequest;
import com.farmer.farmermanagement.dto.UserDTO;
import com.farmer.farmermanagement.dto.UserResponseDTO;
import com.farmer.farmermanagement.entity.User;
import com.farmer.farmermanagement.exception.UserNotFoundException;
import com.farmer.farmermanagement.security.JwtUtil;
import com.farmer.farmermanagement.service.CountryStateCityService;
import com.farmer.farmermanagement.service.EmailService;
import com.farmer.farmermanagement.service.OtpService;
import com.farmer.farmermanagement.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserService userService;
	private final OtpService otpService;
	private final EmailService emailService;
	private final CountryStateCityService countryService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
			String token = jwtUtil.generateToken(authentication);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			return ResponseEntity.ok().headers(headers).body("logged in successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Login failed: " + e.getMessage());
		}
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
		User user = userService.registerUser(userDTO);
		return ResponseEntity.ok(UserResponseDTO.fromEntity(user, "User registered successfully."));
	}

	@PostMapping("/send-otp")
	public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
		String emailOrPhone = request.get("emailOrPhone");
		try {
			String otp = otpService.generateAndSendOtp(emailOrPhone);
			emailService.sendOtpEmail(emailOrPhone, otp);
			return ResponseEntity.ok("OTP sent successfully to " + emailOrPhone);
		} catch (Exception e) {
			return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
					.body("Failed to send OTP: " + e.getMessage());
		}
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String otp = request.get("otp");

		if (otpService.verifyOtp(email, otp)) {
			return ResponseEntity.ok("OTP verified successfully.");
		} else {
			return ResponseEntity.badRequest().body("Invalid or expired OTP.");
		}
	}

@PostMapping("/forgot-user-id")
public ResponseEntity<String> forgotUserId(@RequestBody ForgotUserIdDTO forgotUserIdDTO) {
    try {
        String emailOrPhone = forgotUserIdDTO.getEmailOrPhone();
        return ResponseEntity.ok(userService.forgotUserId(emailOrPhone));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Failed to send user ID: " + e.getMessage());
    }
}

@PostMapping("/forgot-password")
public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
    try {
        String emailOrPhone = dto.getEmailOrPhone();
        return ResponseEntity.ok(userService.forgotPassword(emailOrPhone));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Failed to send reset password: " + e.getMessage());
    }
}


	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam String emailOrPhone, @RequestParam String otp,
			@RequestParam String newPassword) {
		if (userService.verifyOtpAndResetPassword(emailOrPhone, otp, newPassword)) {
			return ResponseEntity.ok("Password reset successfully.");
		}
		return ResponseEntity.badRequest().body("Invalid OTP or expired OTP.");
	}

	@GetMapping("/countries")
	public String getCountries() {
		return countryService.getCountries();
	}

	@GetMapping("/states/{countryCode}")
	public String getStates(@PathVariable String countryCode) {
		return countryService.getStates(countryCode);
	}

	@GetMapping("/districts/{countryCode}/{stateCode}")
	public String getDistricts(@PathVariable String countryCode, @PathVariable String stateCode) {
		return countryService.getDistricts(countryCode, stateCode);
	}

	@GetMapping("/test")
	public String test() {
		return "this is a test endpoint";
	}
}
