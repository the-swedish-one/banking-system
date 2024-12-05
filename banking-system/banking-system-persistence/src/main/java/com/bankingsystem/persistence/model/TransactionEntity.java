package com.bankingsystem.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "transaction")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = true)
    private String fromAccountIban;

    @Column(nullable = true)
    private String toAccountIban;

    public TransactionEntity(BigDecimal amount, String fromAccountIban, String toAccountIban) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (fromAccountIban == null && toAccountIban == null) {
            throw new IllegalArgumentException("At least one of fromAccountId or toAccountId must be specified");
        }
        this.amount = amount;
        this.fromAccountIban = fromAccountIban;
        this.toAccountIban = toAccountIban;
    }

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
