package com.bank.account.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CustomInfoContributor implements InfoContributor {
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${app.version}")
    private String version;
    @Value("${app.artifactId}")
    private String artifactId;
    private final LocalDateTime launchTime = LocalDateTime.now();

    @Override
    public void contribute(Info.Builder builder) {

        builder.withDetail("application name", applicationName)
                .withDetail("artifactId", artifactId)
                .withDetail("launch time", launchTime)
                .withDetail("application version", version)
                .withDetail("context path", contextPath);
    }
}
