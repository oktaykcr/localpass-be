package com.localpass.backend.model.password;

public class PasswordEntityRequest {

    private PasswordEntity passwordEntity;
    private String username;

    public PasswordEntityRequest() {
    }

    public PasswordEntity getPasswordEntity() {
        return passwordEntity;
    }

    public void setPasswordEntity(PasswordEntity passwordEntity) {
        this.passwordEntity = passwordEntity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
