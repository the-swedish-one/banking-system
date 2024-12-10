package com.bankingsystem.api;

import com.bankingsystem.domain.config.OverdraftConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.bankingsystem.api", "com.bankingsystem.domain", "com.bankingsystem.persistence"})
@EntityScan(basePackages = "com.bankingsystem.persistence.model")
@EnableJpaRepositories(basePackages = "com.bankingsystem.persistence.repository")
@EnableScheduling
@EnableConfigurationProperties(OverdraftConfig.class)
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}