package com.bankingsystem.models;

import java.math.BigDecimal;

public interface Withdrawable {

    void withdraw(BigDecimal amount);
}
