package com.bankingsystem.persistence.model;

import com.bankingsystem.persistence.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table(name = "joint_checking_account")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JointCheckingAccountEntity {

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

    @ManyToOne
    @JoinColumn(name = "second_owner_id", referencedColumnName = "userId", nullable = false)
    private UserEntity secondOwner;

    @Column(nullable = false, precision = 19, scale = 4)
    protected BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected CurrencyCode currency;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal overdraftLimit;

}
