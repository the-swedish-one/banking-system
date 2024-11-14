package com.bankingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiBank {

    private int id;
    private String bankName;
    private String bic;
    private List<ApiUser> users = new ArrayList<>();
    private List<ApiAccount> accounts = new ArrayList<>();

    public ApiBank(String bankName, String bic) {
        this.bankName = bankName;
        this.bic = bic;
    }

}
