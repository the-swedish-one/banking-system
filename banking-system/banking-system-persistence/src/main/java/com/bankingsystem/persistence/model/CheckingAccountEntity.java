package com.bankingsystem.persistence.model;

import com.bankingsystem.persistence.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

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

    @Column(name = "account_name", nullable = true)
    protected String accountName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "userId", nullable = false)
    protected UserEntity owner;

    @Column(nullable = false, precision = 19, scale = 2)
    protected BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected CurrencyCode currency;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal overdraftLimit;

    @Column(name = "overdraft_timestamp")
    private Instant overdraftTimestamp;

    public CheckingAccountEntity(UserEntity owner, BigDecimal balance, CurrencyCode currency, BigDecimal overdraftLimit) {
        this.owner = owner;
        this.balance = balance;
        this.currency = currency;
        this.overdraftLimit = overdraftLimit;
    }

    public void setBalance(BigDecimal balance) {
        if (balance != null) {
            this.balance = balance.setScale(2, RoundingMode.HALF_UP);
        } else {
            this.balance = null;
        }
    }

    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        if (overdraftLimit != null) {
            this.overdraftLimit = overdraftLimit.setScale(2, RoundingMode.HALF_UP);
        } else {
            this.overdraftLimit = null;
        }
    }

}
