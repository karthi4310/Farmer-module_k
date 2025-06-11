package com.farmer.farmermanagement.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.farmer.farmermanagement.dto.LoginRequest;
import com.farmer.farmermanagement.dto.ResetPasswordDTO;
import com.farmer.farmermanagement.dto.UserDTO;
import com.farmer.farmermanagement.dto.UserResponseDTO;
import com.farmer.farmermanagement.entity.User;
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

    /* ───────────── LOGIN ───────────── */

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
            String token = jwtUtil.generateToken(authentication);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            return ResponseEntity.ok().headers(headers).body("Logged in successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body("Login failed: " + e.getMessage());
        }
    }

    /* ───────────── SEND OTP (generic) ───────────── */
@PostMapping("/send-otp")
public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
    String emailOrPhone = request.get("emailOrPhone");
    if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
        return ResponseEntity.badRequest().body("Email or phone number is required.");
    }

    String otp = otpService.generateAndSendOtp(emailOrPhone.trim());
    emailService.sendOtpEmail(emailOrPhone.trim(),
        "Your OTP is: " + otp + ". It is valid for 10 minutes.");

    return ResponseEntity.ok("OTP sent successfully to your registered email/phone.");
}

/* ───────────── VERIFY OTP ───────────── */
@PostMapping("/verify-otp")
public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
    String emailOrPhone = request.get("emailOrPhone");
    String otp = request.get("otp");

    if (emailOrPhone == null || emailOrPhone.trim().isEmpty() ||
        otp == null || otp.trim().isEmpty()) {
        return ResponseEntity.badRequest().body("Email/Phone and OTP are required.");
    }

    boolean verified = otpService.verifyOtp(emailOrPhone.trim(), otp.trim());
    return verified
        ? ResponseEntity.ok("OTP verified successfully.")
        : ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                        .body("Invalid or expired OTP.");
}


    /* ───────────── REGISTRATION ───────────── */

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        return ResponseEntity.ok(UserResponseDTO.fromEntity(user, "User registered successfully."));
    }

    /* ───────────── FORGOT USER ID ───────────── */

    @PostMapping("/forgot-user-id")
    public ResponseEntity<String> forgotUserId(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }
        String result = userService.forgotUserId(emailOrPhone);
        return ResponseEntity.ok(result);
    }

    /* ───────────── FORGOT PASSWORD ───────────── */

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }

        // Generate OTP for password reset
        String otp = otpService.generateAndSendOtp(emailOrPhone.trim());

        // Send OTP email with message
        emailService.sendOtpEmail(emailOrPhone.trim(),
                "Your password reset OTP is: " + otp +
                ". Use this OTP to reset your password. It is valid for 10 minutes.");

        return ResponseEntity.ok("Password reset OTP sent successfully to your registered email or phone.");
    }

    /* ───────────── RESET PASSWORD (NO OTP) ───────────── */

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordDTO request) {

        if (request.getEmailOrPhone() == null || request.getNewPassword() == null ||
            request.getConfirmPassword() == null ||
            request.getEmailOrPhone().trim().isEmpty() ||
            request.getNewPassword().trim().isEmpty() ||
            request.getConfirmPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email/Phone and both password fields are required.");
        }

        try {
            boolean success = userService.resetPasswordWithoutOtp(
                    request.getEmailOrPhone().trim(),
                    request.getNewPassword().trim(),
                    request.getConfirmPassword().trim());

            if (success) {
                emailService.sendEmail(
                    new com.farmer.farmermanagement.dto.EmailServiceDTO(
                        request.getEmailOrPhone().trim(),
                        "Password Reset Successful",
                        "Dear user,\n\nYour password has been changed successfully. If you did not perform this action, please contact support immediately.\n\nRegards,\nDigitalAgristack Team"
                    )
                );
                return ResponseEntity.ok("Password changed successfully.");
            } else {
                return ResponseEntity.status(500).body("Password change failed.");
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred: " + ex.getMessage());
        }
    }

    /* ───────────── LOCATION DATA ───────────── */

    @GetMapping("/countries")
    public ResponseEntity<String> getCountries() {
        return ResponseEntity.ok(countryService.getCountries());
    }

    @GetMapping("/states/{countryCode}")
    public ResponseEntity<String> getStates(@PathVariable String countryCode) {
        return ResponseEntity.ok(countryService.getStates(countryCode));
    }

    @GetMapping("/districts/{countryCode}/{stateCode}")
    public ResponseEntity<String> getDistricts(@PathVariable String countryCode,
                                               @PathVariable String stateCode) {
        return ResponseEntity.ok(countryService.getDistricts(countryCode, stateCode));
    }

    /* ───────────── TEST ───────────── */

    @GetMapping("/test")
    public String test() {
        return "This is a test endpoint.";
    }
}
