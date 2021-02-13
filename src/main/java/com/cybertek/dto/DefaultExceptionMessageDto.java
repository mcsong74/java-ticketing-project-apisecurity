package com.cybertek.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DefaultExceptionMessageDto {
    //in order to carry the message, need to create this class

    private String message;

}
