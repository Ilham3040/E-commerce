package com.example.shoppingapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping("/api/app-status")
    public String home() {
        return "The application is working!";
    }
}
