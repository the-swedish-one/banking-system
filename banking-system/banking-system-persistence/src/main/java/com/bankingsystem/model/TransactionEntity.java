package com.bankingsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int transactionId;

    @Column(nullable = false)
    protected BigDecimal amount;

    @Column(nullable = false, updatable = false)
    protected LocalDateTime timestamp;

    public TransactionEntity(BigDecimal amount) {
        this.amount = amount;
    }

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
