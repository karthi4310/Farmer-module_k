package com.farmer.farmermanagement.controller;

import com.farmer.farmermanagement.dto.EmailServiceDTO;
import com.farmer.farmermanagement.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailServiceDTO emailDto) {
        try {
            emailService.sendEmail(emailDto);
            return ResponseEntity.ok("Email sent successfully to " + emailDto.getTo());
        } catch (Exception e) {
            e.printStackTrace(); // Print full error stack trace in logs
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }
}
