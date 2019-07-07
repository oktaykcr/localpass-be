package com.localpass.backend.controller;

import com.localpass.backend.common.model.ListResponse;
import com.localpass.backend.model.PasswordEntity;
import com.localpass.backend.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/password")
public class PasswordController {

    private PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/list")
    public ListResponse listPasswords() {
        return passwordService.listPasswords();
    }

    @PostMapping("/save")
    public PasswordEntity savePassword(@RequestBody PasswordEntity passwordEntity) {
        return passwordService.addPassword(passwordEntity);
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
