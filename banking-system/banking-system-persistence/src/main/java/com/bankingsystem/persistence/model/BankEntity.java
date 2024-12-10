package com.bankingsystem.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bank")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Unique identifier for JPA

    @Column(nullable = false)
    private String bankName;

    @Column(unique = true, nullable = false)
    private String bic;

    @Column(nullable = true)
    private BigDecimal collectedInterest;

}

