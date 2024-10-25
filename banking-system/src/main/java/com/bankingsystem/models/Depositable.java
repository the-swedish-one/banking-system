package com.bankingsystem.models;

import java.math.BigDecimal;

public interface Depositable {

    void deposit(BigDecimal amount);
}