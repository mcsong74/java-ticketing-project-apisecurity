package com.cybertek.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class MailDTO {

    private String emailTo;
    private String emailFrom;
    private String message;
    private String token; //not jwt token, a unique token to confirm in email and one time use only token
    private String subject;
    private String url; //url link user to click on in the email.




}
