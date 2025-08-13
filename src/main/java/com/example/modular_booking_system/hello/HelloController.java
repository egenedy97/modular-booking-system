package com.example.modular_booking_system.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Payment Module!";
    }
}
