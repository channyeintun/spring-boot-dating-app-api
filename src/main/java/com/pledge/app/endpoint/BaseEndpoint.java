package com.pledge.app.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BaseEndpoint {
    @GetMapping
    public String index(){
        return "Happy Coding :D";
    }

    @GetMapping("/api")
    public String api(){
        return "API is working...";
    }
}
