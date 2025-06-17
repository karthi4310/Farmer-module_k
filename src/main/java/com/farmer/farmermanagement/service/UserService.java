package com.farmer.farmermanagement.service;

import com.farmer.farmermanagement.dto.EmailServiceDTO;
import com.farmer.farmermanagement.dto.UserDTO;
import com.farmer.farmermanagement.entity.User;
import com.farmer.farmermanagement.exception.UserAlreadyExistsException;
import com.farmer.farmermanagement.exception.UserNotFoundException;
import com.farmer.farmermanagement.mapper.UserMapper;
import com.farmer.farmermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final OtpService otpService;

    // ✅ Register a new user with OTP verification
    public User registerUser(UserDTO userDTO) {
        log.info("Registering user with email: {}", userDTO.getEmail());

        // Check if email already registered
        userRepository.findByEmail(userDTO.getEmail()).ifPresent(user -> {
            log.warn("Attempted registration with existing email: {}", userDTO.getEmail());
            throw new UserAlreadyExistsException("Email already registered: " + userDTO.getEmail());
        });

        // Check if OTP was verified
        if (!otpService.isVerified(userDTO.getEmail())) {
            log.warn("Registration attempt without verifying OTP for email: {}", userDTO.getEmail());
            throw new IllegalStateException("OTP not verified. Please verify your email before registering.");
        }

        // Map and save user
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        // Clear OTP verification state
        otpService.clearVerification(userDTO.getEmail());

        // Send welcome email
        try {
            emailService.sendRegistrationEmail(savedUser.getEmail(), savedUser.getFirstName());
            log.info("Registration email sent to {}", savedUser.getEmail());
        } catch (Exception e) {
            log.error("Failed to send registration email to {}: {}", savedUser.getEmail(), e.getMessage());
        }

        return savedUser;
    }

    // ✅ Forgot User ID — send OTP
    public String forgotUserId(String emailOrPhone) {
        log.info("Forgot user ID request for: {}", emailOrPhone);
        validateUserExists(emailOrPhone);

        String otp = otpService.generateAndSendOtp(emailOrPhone);
        try {
            emailService.sendOtpEmail(emailOrPhone,
                    "Your OTP for recovering your User ID is: " + otp +
                    ". Use this OTP to verify your identity. It is valid for 10 minutes.");
            log.info("OTP sent for user ID recovery to {}", emailOrPhone);
        } catch (Exception e) {
            log.error("Failed to send OTP for user ID recovery to {}: {}", emailOrPhone, e.getMessage());
            throw new RuntimeException("Failed to send OTP. Please try again.");
        }

        return "OTP has been sent to your registered " + (emailOrPhone.contains("@") ? "email" : "phone") + ".";
    }

    // ✅ Reset password without OTP
    public boolean resetPasswordWithoutOtp(String emailOrPhone, String newPassword, String confirmPassword) {
        log.info("Resetting password without OTP for: {}", emailOrPhone);

        if (!newPassword.equals(confirmPassword)) {
            log.warn("Password and confirm password do not match");
            throw new IllegalArgumentException("Password and confirm password do not match");
        }

        User user = findUserByEmailOrPhone(emailOrPhone);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password reset successfully for user: {}", emailOrPhone);

        // Send confirmation email
        try {
            String subject = "Password Reset Confirmation";
            String body = "Dear " + (user.getFirstName() != null ? user.getFirstName() : "User") + ",\n\n"
                    + "Your password has been changed successfully.\n"
                    + "If you did not perform this action, please contact support immediately.\n\n"
                    + "Regards,\nFarmer Management Team";

            EmailServiceDTO emailDto = EmailServiceDTO.builder()
                    .to(user.getEmail())
                    .subject(subject)
                    .body(body)
                    .build();

            emailService.sendEmail(emailDto);
            log.info("Password reset confirmation email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password reset confirmation email to {}: {}", user.getEmail(), e.getMessage());
        }

        return true;
    }

    // ✅ Validate if a user exists by email or phone
    public void validateUserExists(String emailOrPhone) {
        findUserByEmailOrPhone(emailOrPhone); // throws exception if not found
    }

    // ✅ Get user object for given email or phone
    public User getUserByEmailOrPhone(String emailOrPhone) {
        return findUserByEmailOrPhone(emailOrPhone);
    }

    // 🔒 Private helper to find user
    private User findUserByEmailOrPhone(String emailOrPhone) {
        return userRepository.findByEmail(emailOrPhone)
                .or(() -> userRepository.findByPhoneNumber(emailOrPhone))
                .orElseThrow(() -> new UserNotFoundException("User not found with email or phone: " + emailOrPhone));
    }
}
