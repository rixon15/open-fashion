package com.openfashion.openfasion_marketplace.controllers;

import com.openfashion.openfasion_marketplace.models.entities.User;
import com.openfashion.openfasion_marketplace.services.AuthenticationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {return authenticationService.register(user);}

    @PostMapping("/login")
    public String login(@RequestBody User user) {return authenticationService.login(user);}

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String token) {
        authenticationService.logout(token);
        return "Logged out successfully";
    }

    @GetMapping("/test")
    public String test() {return "TEST";}
}
