package com.bankingsystem.repository;

import com.bankingsystem.model.SavingsAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsAccountRepository  extends JpaRepository<SavingsAccountEntity, Integer> {
}
