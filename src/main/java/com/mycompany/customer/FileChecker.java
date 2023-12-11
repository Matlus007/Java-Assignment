package com.mycompany.customer;
import java.io.File;
import java.io.IOException;

public class FileChecker {
    private String filePath;

    // Constructor
    public FileChecker(String filePath) {
        this.filePath = filePath;
    }

    // Method to check and create the file
    public void checkAndCreateFile() {
        File file = new File(filePath);

        if (file.exists()) {
            System.out.println("The file " + filePath + " already exists.");
        } else {
            try {
                if (file.createNewFile()) {
                    System.out.println("New file " + filePath + " created successfully.");
                } else {
                    System.out.println("Unable to create the file " + filePath + ".");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file: " + e.getMessage());
            }
        }
    }
}
