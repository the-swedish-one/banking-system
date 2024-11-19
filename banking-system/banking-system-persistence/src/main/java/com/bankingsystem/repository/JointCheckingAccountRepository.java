package com.bankingsystem.repository;

import com.bankingsystem.model.JointCheckingAccountEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JointCheckingAccountRepository extends JpaRepository<JointCheckingAccountEntity, Integer> {
}
