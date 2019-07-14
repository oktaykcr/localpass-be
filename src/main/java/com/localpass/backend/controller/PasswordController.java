package com.localpass.backend.controller;

import com.localpass.backend.common.model.ListResponse;
import com.localpass.backend.model.password.PasswordEntity;
import com.localpass.backend.model.password.PasswordEntityRequest;
import com.localpass.backend.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/password")
public class PasswordController {

    private PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/list")
    public ListResponse listPasswords(String username) {
        return passwordService.listPasswords(username);
    }

    @PostMapping("/save")
    public PasswordEntity savePassword(@RequestBody PasswordEntityRequest request) {
        return passwordService.addPassword(request);
    }

    @PutMapping("/update")
    public PasswordEntity updatePassword(@RequestBody PasswordEntity passwordEntity) {
        return passwordService.updatePassword(passwordEntity);
    }

    @DeleteMapping("/delete")
    public Boolean deletePassword(@RequestParam Long id) {
        return passwordService.deletePassword(id);
    }
}
