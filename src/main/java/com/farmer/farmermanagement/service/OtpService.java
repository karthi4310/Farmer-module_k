package com.farmer.farmermanagement.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final FirebaseAuth firebaseAuth;

    // In-memory OTP store — ideally use Redis/Caffeine with expiry
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    // Firebase ID token verification (for Firebase-based auth)
    public boolean verifyOtp(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            log.info("Firebase token verified for user: {}", decodedToken.getEmail());
            return decodedToken != null;
        } catch (FirebaseAuthException e) {
            log.warn("Firebase token verification failed: {}", e.getMessage());
            return false;
        }
    }

    // Overloaded method to verify manual OTP using email or phone
    public boolean verifyOtp(String emailOrPhone, String otp) {
        String storedOtp = otpStore.get(emailOrPhone);
        boolean isValid = storedOtp != null && storedOtp.equals(otp);

        log.info("Verifying OTP for {}: provided={}, expected={}, result={}",
                emailOrPhone, otp, storedOtp, isValid);

        if (isValid) {
            otpStore.remove(emailOrPhone); // OTP is single-use
            log.info("OTP verified and removed from store for {}", emailOrPhone);
        }

        return isValid;
    }

    // Extract user identity (email/phone) from Firebase token
    public String getUserEmailOrPhoneFromToken(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            String emailOrPhone = decodedToken != null ? decodedToken.getEmail() : null;
            log.info("Extracted user identifier from token: {}", emailOrPhone);
            return emailOrPhone;
        } catch (FirebaseAuthException e) {
            log.error("Failed to extract user identifier from token: {}", e.getMessage());
            return null;
        }
    }

    // Generate and send OTP
    public String generateAndSendOtp(String phoneNumber) {
        String otp = String.format("%06d", new Random().nextInt(999999));

        otpStore.put(phoneNumber, otp);
        log.info("Generated OTP for phone {}: {}", phoneNumber, otp);

        sendOtpThroughServerSide(phoneNumber, otp);
        return otp;
    }

    // Method to verify OTP manually
    public boolean verifyOtpCode(String phoneNumber, String otp) {
        String storedOtp = otpStore.get(phoneNumber);
        boolean isValid = storedOtp != null && storedOtp.equals(otp);

        log.info("Verifying OTP for phone {}: provided={}, expected={}, result={}",
                phoneNumber, otp, storedOtp, isValid);

        if (isValid) {
            otpStore.remove(phoneNumber);
            log.info("OTP verified and removed from store for phone {}", phoneNumber);
        }

        return isValid;
    }

    // Simulate sending OTP (integrate SMS/Firebase logic here)
    private void sendOtpThroughServerSide(String phoneNumber, String otp) {
        log.info("OTP sent to phone {}: {}", phoneNumber, otp);
    }
}