package com.microservice.users.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home.html";
    }
}
