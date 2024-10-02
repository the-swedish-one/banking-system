package com.bankingsystem.models;

public class WithdrawTransaction extends Transaction {

    private Account account;

    public WithdrawTransaction(String transactionId, double amount, Account account) {
        super(transactionId, amount);
        this.account = account;
    }

    @Override
    public void execute() {
        try {
            account.withdraw(amount);
            account.getTransactionHistory().add(this);
            System.out.println("Success! Amount withdrawn: " + amount);
        } catch (Exception e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    public Account getAccount() {
        return account;
    }
}
