package com.intuit.workcraft.servicecontractapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthCheck")
public class HealthCheckController {

    @GetMapping
   public ResponseEntity<String> getAppStatus() {
       return ResponseEntity.ok("App is up and running");
   }
}
