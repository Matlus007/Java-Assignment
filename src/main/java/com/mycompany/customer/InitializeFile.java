package com.mycompany.customer;
import java.io.File;
import java.io.IOException;

public class InitializeFile {
    private String fileName;

    public InitializeFile(String fileName) {
        this.fileName = fileName;
    }

    public void InitializeFile() {
        File file = new File(fileName);

        try {
            // Check if the file exists
            if (!file.exists()) {
                // If not, create the file
                if (file.createNewFile()) {
                    System.out.println("File created: " + fileName);
                } else {
                    System.err.println("Failed to create the file: " + fileName);
                }
            } else {
                System.out.println("File already exists: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    // Static method to be called from any other class
    public static void InitializeFile(String fileName) {
        InitializeFile initializeFile = new InitializeFile(fileName);
        initializeFile.InitializeFile();
    }
}