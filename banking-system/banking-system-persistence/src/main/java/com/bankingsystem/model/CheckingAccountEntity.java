package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class CheckingAccountEntity extends AccountEntity {

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal overdraftLimit;

    public CheckingAccountEntity(UserEntity owner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        super(owner, balance, currency);
        this.overdraftLimit = overdraftLimit;
    }
}
