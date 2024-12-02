package com.bankingsystem.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiBank {

    private int id;
    private String bankName;
    private String bic;

    public ApiBank(String bankName, String bic) {
        this.bankName = bankName;
        this.bic = bic;
    }

}
