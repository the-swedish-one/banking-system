package com.bankingsystem.persistence.repository;

import com.bankingsystem.persistence.model.BankEntity;
import com.bankingsystem.persistence.model.CheckingAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccountEntity, Integer> {
    Optional<CheckingAccountEntity> findByIban(String iban);
    List<CheckingAccountEntity> findByBalanceLessThanAndOverdraftTimestampIsNotNull(BigDecimal amount);
}
