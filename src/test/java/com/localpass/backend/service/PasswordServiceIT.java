package com.localpass.backend.service;

import com.localpass.backend.common.model.ListResponse;
import com.localpass.backend.common.util.AES;
import com.localpass.backend.model.password.PasswordEntity;
import com.localpass.backend.model.password.PasswordEntityRequest;
import com.localpass.backend.model.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PasswordServiceIT {

    @Autowired
    public PasswordService passwordService;

    @Autowired
    public CustomUserDetailsServiceImpl customUserDetailsService;

    private User createUser() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        return user;
    }

    private PasswordEntity createPasswordEntity() {
        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setDescription("description");
        passwordEntity.setEmail("email@email.com");
        passwordEntity.setPassword("password");
        passwordEntity.setUsername("username");
        passwordEntity.setName("name");

        return passwordEntity;
    }

    private PasswordEntityRequest createPasswordEntityRequest(PasswordEntity passwordEntity, String username) {
        PasswordEntityRequest passwordEntityRequest = new PasswordEntityRequest();
        passwordEntityRequest.setUsername(username);
        passwordEntityRequest.setPasswordEntity(passwordEntity);

        return passwordEntityRequest;
    }

    @Test
    public void listPasswords() {
        PasswordEntity passwordEntity = createPasswordEntity();
        PasswordEntity passwordEntity2 = createPasswordEntity();

        User user = createUser();
        User savedUser = customUserDetailsService.save(user);
        passwordEntity.setUser(savedUser);
        passwordEntity2.setUser(savedUser);

        PasswordEntityRequest passwordEntityRequest = createPasswordEntityRequest(passwordEntity, savedUser.getUsername());
        PasswordEntityRequest passwordEntityRequest2 = createPasswordEntityRequest(passwordEntity2, savedUser.getUsername());

        PasswordEntity savedPasswordEntity = passwordService.addPassword(passwordEntityRequest);
        PasswordEntity savedPasswordEntity2 = passwordService.addPassword(passwordEntityRequest2);

        ListResponse listResponse = passwordService.listPasswords(savedUser.getUsername());

        assertThat(listResponse.getTotalCount()).isEqualTo(2);
        assertThat(passwordEntity.getName()).isEqualTo(((PasswordEntity) listResponse.getData().get(0)).getName());
        assertThat(passwordEntity2.getName()).isEqualTo(((PasswordEntity) listResponse.getData().get(1)).getName());
    }

    @Test
    public void addPassword() {
        PasswordEntity passwordEntity = createPasswordEntity();

        User user = createUser();
        User savedUser = customUserDetailsService.save(user);
        passwordEntity.setUser(savedUser);

        PasswordEntityRequest passwordEntityRequest = createPasswordEntityRequest(passwordEntity, savedUser.getUsername());

        PasswordEntity savedPasswordEntity = passwordService.addPassword(passwordEntityRequest);

        String expectedPasswordName = AES.encrypt(passwordEntity.getName(), savedUser.getUsername() + "_secret");

        assertThat(savedPasswordEntity).isNotNull();
        assertThat(savedPasswordEntity.getName()).isEqualTo(expectedPasswordName);
    }

    @Test
    public void updatePassword() {
        PasswordEntity passwordEntity = createPasswordEntity();

        User user = createUser();
        User savedUser = customUserDetailsService.save(user);
        passwordEntity.setUser(savedUser);

        PasswordEntityRequest passwordEntityRequest = createPasswordEntityRequest(passwordEntity, savedUser.getUsername());

        PasswordEntity savedPasswordEntity = passwordService.addPassword(passwordEntityRequest);

        PasswordEntity updateRequest = new PasswordEntity();
        updateRequest.setId(savedPasswordEntity.getId());
        updateRequest.setName("updatedName");
        updateRequest.setPassword("updatedPassword");
        updateRequest.setUsername("updatedUsername");
        updateRequest.setEmail("updatedemail@email.com");
        updateRequest.setDescription("updatedDescription");

        PasswordEntity updatedPasswordEntity = passwordService.updatePassword(updateRequest);

        String secret = savedUser.getUsername() + "_secret";

        String actualPasswordName = AES.decrypt(updatedPasswordEntity.getName(), secret);
        String actualPasswordPassword = AES.decrypt(updatedPasswordEntity.getPassword(), secret);
        String actualPasswordUsername = AES.decrypt(updatedPasswordEntity.getUsername(), secret);
        String actualPasswordEmail = AES.decrypt(updatedPasswordEntity.getEmail(), secret);
        String actualPasswordDescription = AES.decrypt(updatedPasswordEntity.getDescription(), secret);

        System.out.println(AES.decrypt(updatedPasswordEntity.getName(), secret));

        assertThat(updatedPasswordEntity).isNotNull();
        assertThat(actualPasswordName).isEqualTo(updateRequest.getName());
        assertThat(actualPasswordPassword).isEqualTo(updateRequest.getPassword());
        assertThat(actualPasswordUsername).isEqualTo(updateRequest.getUsername());
        assertThat(actualPasswordEmail).isEqualTo(updateRequest.getEmail());
        assertThat(actualPasswordDescription).isEqualTo(updateRequest.getDescription());
    }

    @Test
    public void deletePassword() {
        PasswordEntity passwordEntity = createPasswordEntity();

        User user = createUser();
        User savedUser = customUserDetailsService.save(user);
        passwordEntity.setUser(savedUser);

        PasswordEntityRequest passwordEntityRequest = createPasswordEntityRequest(passwordEntity, savedUser.getUsername());

        PasswordEntity savedPasswordEntity = passwordService.addPassword(passwordEntityRequest);

        Boolean result = passwordService.deletePassword(savedPasswordEntity.getId());

        assertThat(result).isTrue();
    }
}
