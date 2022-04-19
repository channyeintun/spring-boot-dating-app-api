package com.pledge.app.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/ping")
public class PingEndPoint {
    @RolesAllowed({"ROLE_DEVELOPER"})
    @GetMapping
    public String ping(){
        return "PONG!";
    }
}
