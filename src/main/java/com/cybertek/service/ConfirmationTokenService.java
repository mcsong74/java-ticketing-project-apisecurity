package com.cybertek.service;

import com.cybertek.entity.ConfirmationToken;
import com.cybertek.exception.TicketingProjectException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;


public interface ConfirmationTokenService {
    //need ConfirmationToken object returned to send email to the user
    ConfirmationToken save (ConfirmationToken confirmationToken);

    void sendEmail(SimpleMailMessage email);

    ConfirmationToken readByToken(String token) throws TicketingProjectException;

    void delete(ConfirmationToken confirmationToken);


}
