package com.skyshare.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class MyController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Servlet!";
    }

    // Add more methods to handle other endpoints
}
