package com.bankingsystem.repository;

import com.bankingsystem.model.PersonDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDetailsRepository extends JpaRepository <PersonDetailsEntity, Integer> {
}
