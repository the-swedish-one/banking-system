package com.bankingsystem.repository;

import com.bankingsystem.model.CurrencyConversionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversionEntity, Integer> {
    Optional<CurrencyConversionEntity> findTopByOrderByTimestampDesc();
}
