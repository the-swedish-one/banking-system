package com.bankingsystem.api;

import com.bankingsystem.persistence.repository.BankRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = com.bankingsystem.api.Main.class)
public class RepositoryTest {

    @Autowired
    private BankRepository bankRepository;

    @Test
    void testRepositoryLoads() {
        assertNotNull(bankRepository, "BankRepository should be loaded as a Spring bean");
    }
}
