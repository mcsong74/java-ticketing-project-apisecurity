package com.cybertek.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi(){
        SecurityScheme securitySchemeItem = new SecurityScheme(); //create an instance
        securitySchemeItem.setType(SecurityScheme.Type.HTTP); //type can be HTTP, oAuth, and etc...
        securitySchemeItem.setScheme("bearer"); //bearer authentication = token authentication
        securitySchemeItem.setBearerFormat("JWT"); //token format= JWT
        securitySchemeItem.setIn(SecurityScheme.In.HEADER); //location of the token = HEADER
        securitySchemeItem.setName("Autherization"); // key
        Info infoVersion = new Info().title("Cybertek Ticketing Application").version("snapshot");
//        io.swagger.v3.oas.models.info.Info infoVersion = new io.swagger.v3.oas.models.info.Info()
//                .title("Cybertek API").version("snapshot");
        SecurityRequirement securityItem = new SecurityRequirement().addList("bearer-jwt",
                Arrays.asList("read", "write"));
        return new OpenAPI().components(new Components()
                .addSecuritySchemes("bearer-jwt",securitySchemeItem))
                .info(infoVersion)
                .addSecurityItem(securityItem);

    }
}
