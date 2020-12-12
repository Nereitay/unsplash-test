package com.example.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "unsplash.authorize")
public class AuthorizeProperties {

    private String codeUrl;
    private String tokenUrl;
    private String client_id;
    private String redirect_uri;
    private String response_type;
    private String  scope;
    private String client_secret;
    private String grant_type;
    private String token;
    private String collectionUrl;
}