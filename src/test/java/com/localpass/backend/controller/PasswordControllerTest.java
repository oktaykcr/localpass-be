package com.localpass.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.localpass.backend.model.password.PasswordEntity;
import com.localpass.backend.service.PasswordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PasswordController.class)
public class PasswordControllerTest {

    private final String BASE_URL = "/api/v1/password";

    @MockBean
    private PasswordService passwordService;

    @Autowired
    private MockMvc mockMvc;

    public String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PasswordEntity createPassword() {
        return
                new PasswordEntity("name",
                        "username",
                        "password",
                        "email",
                        "description");
    }

    @Test
    public void listPasswords_HttpStatusShouldOk() throws Exception {
        mockMvc.perform(get(BASE_URL + "/list"))
                .andExpect(status().isOk());
    }

    @Test
    public void savePassword_HttpStatusShouldOk() throws Exception {
        PasswordEntity passwordEntity = createPassword();
        mockMvc.perform(post(BASE_URL + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(passwordEntity)))

                .andExpect(status().isOk());
    }

    @Test
    public void updatePassword_HttpStatusShouldOk() throws Exception {
        PasswordEntity passwordEntity = createPassword();
        passwordEntity.setId(1L);
        mockMvc.perform(put(BASE_URL + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(passwordEntity)))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePassword_HttpStatusShouldOk() throws Exception {
        PasswordEntity passwordEntity = createPassword();
        passwordEntity.setId(1L);
        mockMvc.perform(delete(BASE_URL + "/delete").param("id", passwordEntity.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}