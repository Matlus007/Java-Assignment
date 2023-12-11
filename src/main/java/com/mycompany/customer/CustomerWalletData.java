package com.mycompany.customer;
public class CustomerWalletData {
    private String name;
    private double balance;

    public CustomerWalletData(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
