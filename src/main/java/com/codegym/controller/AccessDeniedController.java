package com.codegym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "error/accessDenied"; // Trỏ tới file templates/error/accessDenied.html
    }
}
