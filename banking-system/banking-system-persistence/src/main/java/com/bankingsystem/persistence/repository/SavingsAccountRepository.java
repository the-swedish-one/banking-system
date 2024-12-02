package com.bankingsystem.persistence.repository;

import com.bankingsystem.persistence.model.SavingsAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsAccountRepository  extends JpaRepository<SavingsAccountEntity, Integer> {
}
