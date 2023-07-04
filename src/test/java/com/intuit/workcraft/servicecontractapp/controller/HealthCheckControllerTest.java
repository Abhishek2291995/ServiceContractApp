package com.intuit.workcraft.servicecontractapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthCheckControllerTest {

    @Test
    public void testGetAppStatus() {
        // Create an instance of the HealthCheckController
        HealthCheckController healthCheckController = new HealthCheckController();

        // Execute the method
        ResponseEntity<String> response = healthCheckController.getAppStatus();

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("App is up and running", response.getBody());
    }
}
