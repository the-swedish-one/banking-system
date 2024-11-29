package com.bankingsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "bank")
@Entity
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

}

