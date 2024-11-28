package com.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bank {

    private int id;
    private String bankName;
    private String bic;

    public Bank(String bankName, String bic) {
        this.bankName = bankName;
        this.bic = bic;
    }

}
