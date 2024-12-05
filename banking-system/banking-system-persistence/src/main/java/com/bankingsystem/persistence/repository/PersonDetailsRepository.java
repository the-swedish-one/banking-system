package com.bankingsystem.persistence.repository;

import com.bankingsystem.persistence.model.PersonDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDetailsRepository extends JpaRepository <PersonDetailsEntity, Integer> {
    boolean existsByEmail(String email);
}
