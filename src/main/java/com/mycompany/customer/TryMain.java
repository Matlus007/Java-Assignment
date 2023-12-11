package com.mycompany.customer;
public class TryMain {
    private String username = "Matlus";
    private String userID = "Tp005";
    private String contact = "018-1234567";
    private double balance = 50.0;
    
    public TryMain(){
        new CustomerMainMenu(username, userID, contact, balance);
    }
    
    public static void main(String[] args) {
        // Create an instance of TryMain to invoke the constructor
        TryMain tryMain = new TryMain();
    }
}
