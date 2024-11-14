package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int accountId;

    @Column(unique = true, nullable = false)
    protected String iban;

    @Column(name = "account_name", nullable = false)
    protected String accountName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    protected UserEntity owner;

    @Column(nullable = false, precision = 19, scale = 4)
    protected BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected CurrencyCode currency;

    public AccountEntity(UserEntity owner, BigDecimal balance, CurrencyCode currency) {
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
    }
}
