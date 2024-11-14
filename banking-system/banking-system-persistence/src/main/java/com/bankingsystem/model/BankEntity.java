package com.bankingsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class BankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Unique identifier for JPA

    @Column(nullable = false)
    private String bankName;

    @Column(unique = true, nullable = false)
    private String bic;

}

