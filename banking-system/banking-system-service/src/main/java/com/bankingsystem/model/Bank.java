package com.bankingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bank {

    private int id;
    private String bankName;
    private String bic;
    private List<User> users = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();

    public Bank(String bankName, String bic) {
        this.bankName = bankName;
        this.bic = bic;
    }

}
