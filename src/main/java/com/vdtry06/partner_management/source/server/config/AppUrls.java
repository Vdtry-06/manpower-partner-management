package com.vdtry06.partner_management.source.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.urls")
public class AppUrls {
    private String login;
    private String logout;
    private Map<String, String> home;
    private Map<String, String> create;
    private Map<String, String> management;
}
