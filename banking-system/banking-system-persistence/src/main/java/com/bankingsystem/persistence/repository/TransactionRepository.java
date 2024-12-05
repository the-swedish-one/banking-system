package com.bankingsystem.persistence.repository;

import com.bankingsystem.persistence.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    List<TransactionEntity> findByFromAccountIbanOrToAccountIban(String iban, String iban1);
}
