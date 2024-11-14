package com.bankingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class JointCheckingAccountEntity extends CheckingAccountEntity {

    @ManyToOne
    @JoinColumn(name = "second_owner_user_id", nullable = false)
    private UserEntity secondOwner;
}
