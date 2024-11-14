package com.bankingsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class SavingsAccountEntity extends AccountEntity {

    @Column(name = "interest_rate_percentage")
    private double interestRatePercentage;
}
