package com.example.contest_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EncryptionService { // 암호화 서비스
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encrypt(String text) {
        return passwordEncoder.encode(text);
    }

    public boolean match(String rawText, String encryptedText) {
        return passwordEncoder.matches(rawText, encryptedText);
    }
}
