package com.bankingsystem.repository;

import com.bankingsystem.model.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, Integer> {
    Optional<BankEntity> findByBic(String bic);
}
