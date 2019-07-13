package com.localpass.backend.controller;

import com.localpass.backend.model.user.User;
import com.localpass.backend.service.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserController(CustomUserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "/register")
    public User register(@RequestBody User user) {
        return userDetailsService.save(user);
    }

}
