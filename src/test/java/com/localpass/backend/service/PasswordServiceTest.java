package com.localpass.backend.service;

import com.localpass.backend.common.model.ListResponse;
import com.localpass.backend.model.PasswordEntity;
import com.localpass.backend.repository.PasswordRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordServiceTest {

    @MockBean
    private PasswordRepository repository;

    @Autowired
    private PasswordService service;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PasswordEntity createPasswordEntity() {
        PasswordEntity password =
                new PasswordEntity(
                        "Passname",
                        "username",
                        "password",
                        "email@email.com",
                        "description");

        return password;
    }

    @Test
    public void addPassword_shouldAddPassword() {
        PasswordEntity mockPasswordEntity = createPasswordEntity();

        Mockito.when(repository.save(mockPasswordEntity)).thenReturn(mockPasswordEntity);

        PasswordEntity addedPasswordEntity = service.addPassword(mockPasswordEntity);

        assertThat(addedPasswordEntity).isEqualTo(mockPasswordEntity);
    }

    @Test
    public void addPassword_shouldThrowException_passwordNameIsNull() {
        PasswordEntity mockPasswordEntity = createPasswordEntity();
        mockPasswordEntity.setName(null);

        expectedException.expect(NullPointerException.class);

        service.addPassword(mockPasswordEntity);
    }


    @Test
    public void addPassword_shouldThrowException_usernameIsNull() {
        PasswordEntity mockPasswordEntity = createPasswordEntity();
        mockPasswordEntity.setUsername(null);

        expectedException.expect(NullPointerException.class);

        service.addPassword(mockPasswordEntity);
    }


    @Test
    public void addPassword_shouldThrowException_passwordIsNull() {
        PasswordEntity mockPasswordEntity = createPasswordEntity();
        mockPasswordEntity.setPassword(null);

        expectedException.expect(NullPointerException.class);

        service.addPassword(mockPasswordEntity);
    }

    @Test
    public void addPassword_shouldThrowException_emailIsNull() {
        PasswordEntity mockPasswordEntity = createPasswordEntity();
        mockPasswordEntity.setEmail(null);

        expectedException.expect(NullPointerException.class);

        service.addPassword(mockPasswordEntity);
    }

    @Test
    public void updatePassword_shouldUpdatePassword() {
        PasswordEntity passwordEntity = createPasswordEntity();
        passwordEntity.setId(1l);

        Mockito.when(repository.getOne(Mockito.anyLong())).thenReturn(passwordEntity);
        Mockito.when(repository.save(passwordEntity)).thenReturn(passwordEntity);

        PasswordEntity updatedPassword = service.updatePassword(passwordEntity);

        assertThat(updatedPassword.getName()).isEqualTo(passwordEntity.getName());
    }

    @Test
    public void listPasswords_shouldListPasswords() {
        PasswordEntity pe1 = createPasswordEntity();
        pe1.setId(1l);
        PasswordEntity pe2 = createPasswordEntity();
        pe2.setId(2l);
        List<PasswordEntity> passwords = Arrays.asList(pe1, pe2);

        ListResponse mockListResponse = new ListResponse();
        mockListResponse.setData(passwords);
        mockListResponse.setTotalCount(passwords.size());

        Mockito.when(repository.findAll()).thenReturn(passwords);

        ListResponse listResponse = service.listPasswords();

        assertThat(listResponse.getData()).isEqualTo(mockListResponse.getData());
        assertThat(listResponse.getTotalCount()).isEqualTo(mockListResponse.getTotalCount());
    }
}
