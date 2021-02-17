package com.cybertek.service;

import com.cybertek.entity.ConfirmationToken;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public interface ConfirmationTokenService {
    //need ConfirmationToken object returned to send email to the user
    ConfirmationToken save (ConfirmationToken confirmationToken);

    void sendEmail(SimpleMailMessage email);

    ConfirmationToken readByToken(String token);

    void delete(ConfirmationToken confirmationToken);


}
