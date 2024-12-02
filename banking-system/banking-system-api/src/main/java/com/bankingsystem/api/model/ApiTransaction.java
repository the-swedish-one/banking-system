package com.bankingsystem.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiTransaction {

    private int transactionId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private Integer fromAccountId;
    private Integer toAccountId;

}
