package com.bankingsystem.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Duration;

@Data
@Configuration
@ConfigurationProperties(prefix = "account.overdraft")
public class OverdraftConfig {

    private BigDecimal interestRate;
    private long durationSeconds;

    public Duration getOverdraftDuration() {
        return Duration.ofSeconds(durationSeconds);
    }

}
