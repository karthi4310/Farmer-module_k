package com.farmer.farmermanagement.controller;

import com.farmer.farmermanagement.dto.*;
import com.farmer.farmermanagement.entity.User;
import com.farmer.farmermanagement.security.JwtUtil;
import com.farmer.farmermanagement.service.CountryStateCityService;
import com.farmer.farmermanagement.service.EmailService;
import com.farmer.farmermanagement.service.OtpService;
import com.farmer.farmermanagement.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );

            String token = jwtUtil.generateToken(authentication);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(error);
        }
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        return ResponseEntity.ok(UserResponseDTO.fromEntity(user, "User registered successfully."));
    }

    // ✅ SEND OTP (for general use like registration or verification)
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");

        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }

        String otp = otpService.generateAndSendOtp(emailOrPhone.trim());

        emailService.sendOtpEmail(emailOrPhone.trim(),
                "Your OTP is: " + otp + ". It is valid for 10 minutes.");

        return ResponseEntity.ok("OTP sent successfully to your registered email or phone.");
    }

    // ✅ VERIFY OTP
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

    // ✅ FORGOT USER ID
    @PostMapping("/forgot-user-id")
    public ResponseEntity<String> forgotUserId(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }

        String result = userService.forgotUserId(emailOrPhone.trim());
        return ResponseEntity.ok(result);
    }

    // ✅ FORGOT PASSWORD (OTP-based flow)
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }

        String otp = otpService.generateAndSendOtp(emailOrPhone.trim());

        emailService.sendOtpEmail(emailOrPhone.trim(),
                "Your password reset OTP is: " + otp + ". It is valid for 10 minutes.");

        return ResponseEntity.ok("Password reset OTP sent successfully to your registered email or phone.");
    }

    // ✅ RESET PASSWORD WITHOUT OTP
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDTO request) {
        try {
            boolean success = userService.resetPasswordWithoutOtp(
                    request.getEmailOrPhone().trim(),
                    request.getNewPassword().trim(),
                    request.getConfirmPassword().trim()
            );

            if (success) {
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

    // 🌐 Location APIs
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

    // 🔧 Test endpoint
    @GetMapping("/test")
    public String test() {
        return "This is a test endpoint.";
    }
}
