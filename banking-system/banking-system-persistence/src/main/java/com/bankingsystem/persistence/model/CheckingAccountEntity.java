package com.bankingsystem.persistence.model;

import com.bankingsystem.persistence.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table(name = "checking_account")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckingAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int accountId;

    @Column(unique = true, nullable = false)
    protected String iban;

    @Column(name = "account_name", nullable = false)
    protected String accountName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "userId")
    protected UserEntity owner;

    @Column(nullable = false, precision = 19, scale = 4)
    protected BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected CurrencyCode currency;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal overdraftLimit;

    public CheckingAccountEntity(UserEntity owner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
        this.overdraftLimit = overdraftLimit;
    }
}
