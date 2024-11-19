package com.bankingsystem.repository;

import com.bankingsystem.model.CheckingAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccountEntity, Integer> {
}
