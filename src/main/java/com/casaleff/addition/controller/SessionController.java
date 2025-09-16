package com.casaleff.addition.controller;

import com.casaleff.addition.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/login")
    public UUID login(@RequestBody String password) {
        return sessionService.createSession(password);
    }
}
