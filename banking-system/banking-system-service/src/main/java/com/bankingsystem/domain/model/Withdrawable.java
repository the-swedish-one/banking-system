package com.bankingsystem.domain.model;

import java.math.BigDecimal;

public interface Withdrawable {

    void withdraw(BigDecimal amount);
}
