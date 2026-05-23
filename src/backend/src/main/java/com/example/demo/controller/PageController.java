
package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/form")
    public String form() {
        return "form";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

}
