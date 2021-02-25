package com.cybertek.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class DefaultExceptionMessageDto {
    //in order to carry the message, need to create this class

    private String message;

}
