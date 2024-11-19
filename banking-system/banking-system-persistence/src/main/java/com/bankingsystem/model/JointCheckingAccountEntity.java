package com.bankingsystem.model;

import com.bankingsystem.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JointCheckingAccountEntity {

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

    @ManyToOne
    @JoinColumn(name = "second_owner_user_id", nullable = false)
    private UserEntity secondOwner;

    @Column(nullable = false, precision = 19, scale = 4)
    protected BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected CurrencyCode currency;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal overdraftLimit;

}
