package com.bankingsystem.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication(scanBasePackages = {"com.bankingsystem.api", "com.bankingsystem.domain", "com.bankingsystem.persistence"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}