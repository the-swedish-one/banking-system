package com.bankingsystem.persistence.repository;

import com.bankingsystem.persistence.model.JointCheckingAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface JointCheckingAccountRepository extends JpaRepository<JointCheckingAccountEntity, Integer> {
    Optional<JointCheckingAccountEntity> findByIban(String iban);
    List<JointCheckingAccountEntity> findByBalanceLessThanAndOverdraftTimestampIsNotNull(BigDecimal amount);

}
