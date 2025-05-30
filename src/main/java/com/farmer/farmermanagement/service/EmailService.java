package com.farmer.farmermanagement.service;
 
import com.farmer.farmermanagement.dto.EmailServiceDTO;
 
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
 
@Builder
@Slf4j
@Service
public class EmailService {
 
    private final JavaMailSender mailSender;
 
    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
 
    @Async
    public void sendEmail(EmailServiceDTO emailDto) {
        if (emailDto == null || emailDto.getTo() == null || emailDto.getTo().isEmpty()) {
            log.error("Email DTO or recipient address is null or empty");
            throw new IllegalArgumentException("Email DTO and recipient address must not be null or empty");
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailDto.getTo());
            message.setSubject(emailDto.getSubject());
            message.setText(emailDto.getBody());
            mailSender.send(message);
            log.info("Email sent successfully to {}", emailDto.getTo());
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", emailDto.getTo(), e.getMessage(), e);
        }
    }
 
    @Async
    public void sendOtpEmail(String to, String otp) {
        if (to == null || to.isEmpty() || otp == null || otp.isEmpty()) {
            log.error("Recipient or OTP is null or empty");
            throw new IllegalArgumentException("Recipient and OTP must not be null or empty");
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP is: " + otp);
            mailSender.send(message);
            log.info("OTP email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", to, e.getMessage(), e);
        }
    }
 
    @Async
    public void sendRegistrationEmail(String to, String name) {
        if (to == null || to.isEmpty()) {
            log.error("Recipient email is null or empty");
            throw new IllegalArgumentException("Recipient email must not be null or empty");
        }
        try {
            StringBuilder body = new StringBuilder()
                    .append("Dear ").append(name != null ? name : "").append(",\n\n")
                    .append("Thank you for registering with us! ")
                    .append("Your account has been successfully created.\n\n")
                    .append("You can now log in and start managing your profile.\n\n")
                    .append("Regards,\n")
                    .append("The Farmer Management Team");
 
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Welcome to DigitalAgristack");
            message.setText(body.toString());
            mailSender.send(message);
            log.info("Registration email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send registration email to {}: {}", to, e.getMessage(), e);
        }
    }
 
    @Async
    public void sendUserIdEmail(String to, String userId) {
        if (to == null || to.isEmpty() || userId == null || userId.isEmpty()) {
            log.error("Recipient email or user ID is null or empty");
            throw new IllegalArgumentException("Recipient email and user ID must not be null or empty");
        }
        try {
            String subject = "Your User ID - DigitalAgristack";
            String body = "Dear user,\n\nYour registered User ID is: " + userId +
                          "\n\nIf you did not request this, please ignore this email.\n\n" +
                          "Regards,\nDigitalAgristack Team";
 
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
 
            log.info("User ID email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send User ID email to {}: {}", to, e.getMessage(), e);
        }
    }
 
    // NEW method to send password reset confirmation email
    @Async
    public void sendPasswordResetConfirmationEmail(String to, String name) {
        if (to == null || to.isEmpty()) {
            log.error("Recipient email is null or empty");
            throw new IllegalArgumentException("Recipient email must not be null or empty");
        }
        try {
            StringBuilder body = new StringBuilder()
                    .append("Dear ").append(name != null ? name : "User").append(",\n\n")
                    .append("Your password has been changed successfully.\n")
                    .append("If you did not perform this action, please contact support immediately.\n\n")
                    .append("Regards,\n")
                    .append("The Farmer Management Team");
 
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Password Reset Confirmation");
            message.setText(body.toString());
            mailSender.send(message);
 
            log.info("Password reset confirmation email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send password reset confirmation email to {}: {}", to, e.getMessage(), e);
        }
    }
}