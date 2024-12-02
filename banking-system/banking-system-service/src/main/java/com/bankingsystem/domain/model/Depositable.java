package com.bankingsystem.domain.model;

import java.math.BigDecimal;

public interface Depositable {

    void deposit(BigDecimal amount);
}