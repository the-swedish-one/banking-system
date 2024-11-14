package com.bankingsystem.model;

import java.math.BigDecimal;

public interface Withdrawable {

    void withdraw(BigDecimal amount);
}
