package com.bankingsystem.models;

public interface Withdrawable {

    void withdraw(double amount) throws Exception; // TODO custom unchecked exception
}
