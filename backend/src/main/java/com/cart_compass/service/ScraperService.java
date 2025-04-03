package com.cart_compass.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class ScraperService {

    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
    public void runPythonScript() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python3", "/path/to/script.py");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.err.println("Error executing Python script.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
