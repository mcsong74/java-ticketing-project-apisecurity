package com.cybertek.implementation;

import com.cybertek.entity.ConfirmationToken;
import com.cybertek.repository.ConfirmationTokenRepository;
import com.cybertek.service.ConfirmationTokenService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private ConfirmationTokenRepository confirmationTokenRepository;
    private JavaMailSender javaMailSender;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository, JavaMailSender javaMailSender) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public ConfirmationToken save(ConfirmationToken confirmationToken) {
        return null;
    }

    @Override
    public void sendEmail(SimpleMailMessage email) {

    }

    @Override
    public ConfirmationToken readByToken(String token) {
        return null;
    }

    @Override
    public void delete(ConfirmationToken confirmationToken) {

    }
}
