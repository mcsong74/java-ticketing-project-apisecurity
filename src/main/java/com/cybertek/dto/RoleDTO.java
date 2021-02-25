package com.cybertek.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(value = {"hiberanteLazyinitializer"}, ignoreUnknown = true)
public class RoleDTO {

    private Long id;
    private String description;
}
