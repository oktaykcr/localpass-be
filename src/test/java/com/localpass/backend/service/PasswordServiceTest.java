package com.localpass.backend.service;

import com.localpass.backend.common.model.ListResponse;
import com.localpass.backend.exception.ApiError;
import com.localpass.backend.exception.ExceptionEnum;
import com.localpass.backend.model.password.PasswordEntity;
import com.localpass.backend.model.password.PasswordEntityRequest;
import com.localpass.backend.model.user.Role;
import com.localpass.backend.model.user.User;
import com.localpass.backend.repository.PasswordRepository;
import com.localpass.backend.repository.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import sun.nio.cs.US_ASCII;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordServiceTest {

    @MockBean
    private PasswordRepository passwordRepository;

    @MockBean
    private UserRepository userRepository;

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

    private PasswordEntityRequest createPasswordEntityRequest() {
        PasswordEntityRequest request = new PasswordEntityRequest();
        request.setPasswordEntity(createPasswordEntity());
        request.setUsername("username");

        return request;
    }


    private User createUser() {
        User user = new User();
        user.setUsername("username");
        user.setEnabled(true);
        user.setPassword("password");
        user.setId("id");
        user.setRoles(Arrays.asList(new Role("USER")));
        return user;
    }

    @Test
    public void addPassword_shouldAddPassword() {
        PasswordEntityRequest request = createPasswordEntityRequest();
        User mockUser = createUser();

        Mockito.when(userRepository.findByUsername(request.getUsername())).thenReturn(mockUser);
        Mockito.when(passwordRepository.save(request.getPasswordEntity())).thenReturn(request.getPasswordEntity());

        PasswordEntity addedPasswordEntity = service.addPassword(request);

        assertThat(addedPasswordEntity).isEqualTo(request.getPasswordEntity());
    }

    @Test
    public void addPassword_shouldThrowException_passwordNameIsNull() {
        PasswordEntityRequest request = createPasswordEntityRequest();
        request.getPasswordEntity().setName(null);

        expectedException.expect(ApiError.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ExceptionEnum.BAD_REQUEST.getErrorCode()));

        service.addPassword(request);
    }


    @Test
    public void addPassword_shouldThrowException_usernameIsNull() {
        PasswordEntityRequest request = createPasswordEntityRequest();
        request.getPasswordEntity().setUsername(null);

        expectedException.expect(ApiError.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ExceptionEnum.BAD_REQUEST.getErrorCode()));

        service.addPassword(request);
    }


    @Test
    public void addPassword_shouldThrowException_passwordIsNull() {
        PasswordEntityRequest request = createPasswordEntityRequest();
        request.getPasswordEntity().setPassword(null);

        expectedException.expect(ApiError.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ExceptionEnum.BAD_REQUEST.getErrorCode()));

        service.addPassword(request);
    }

    @Test
    public void addPassword_shouldThrowException_emailIsNull() {
        PasswordEntityRequest request = createPasswordEntityRequest();
        request.getPasswordEntity().setEmail(null);

        expectedException.expect(ApiError.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ExceptionEnum.BAD_REQUEST.getErrorCode()));

        service.addPassword(request);
    }

    @Test
    public void addPassword_shouldBeError_userNotFound() {
        PasswordEntityRequest request = createPasswordEntityRequest();

        Mockito.when(userRepository.findByUsername(request.getUsername())).thenReturn(null);
        Mockito.when(passwordRepository.save(request.getPasswordEntity())).thenReturn(request.getPasswordEntity());

        expectedException.expect(ApiError.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ExceptionEnum.NOT_FOUND.getErrorCode()));

        service.addPassword(request);
    }

    @Test
    public void updatePassword_shouldUpdatePassword() {
        PasswordEntity passwordEntity = createPasswordEntity();
        passwordEntity.setId("id");

        Mockito.when(passwordRepository.getOne(Mockito.anyString())).thenReturn(passwordEntity);
        Mockito.when(passwordRepository.save(passwordEntity)).thenReturn(passwordEntity);

        PasswordEntity updatedPassword = service.updatePassword(passwordEntity);

        assertThat(updatedPassword.getName()).isEqualTo(passwordEntity.getName());
    }

    @Test
    public void updatePassword_shouldBeError_userIdIsNull() {
        PasswordEntity passwordEntity = createPasswordEntity();
        passwordEntity.setId(null);

        expectedException.expect(ApiError.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ExceptionEnum.BAD_REQUEST.getErrorCode()));

        service.updatePassword(passwordEntity);
    }

    @Test
    public void updatePassword_shouldBeError_passwordNotFound() {
        PasswordEntity passwordEntity = createPasswordEntity();
        passwordEntity.setId("id");

        Mockito.when(passwordRepository.getOne(passwordEntity.getId())).thenReturn(null);

        expectedException.expect(ApiError.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ExceptionEnum.NOT_FOUND.getErrorCode()));

        service.updatePassword(passwordEntity);
    }

    @Test
    public void listPasswords_shouldListPasswords() {
        PasswordEntity pe1 = createPasswordEntity();
        pe1.setId("id");
        PasswordEntity pe2 = createPasswordEntity();
        pe2.setId("id2");
        List<PasswordEntity> passwords = Arrays.asList(pe1, pe2);

        User mockUser = createUser();

        ListResponse mockListResponse = new ListResponse();
        mockListResponse.setData(passwords);
        mockListResponse.setTotalCount(passwords.size());

        Mockito.when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(mockUser);
        Mockito.when(passwordRepository.findByUserId(mockUser.getId())).thenReturn(passwords);

        ListResponse listResponse = service.listPasswords(mockUser.getUsername());

        assertThat(listResponse.getData()).isEqualTo(mockListResponse.getData());
        assertThat(listResponse.getTotalCount()).isEqualTo(mockListResponse.getTotalCount());
    }

    @Test
    public void listPassword_shouldBeError_userNotFound() {

        User user = createUser();

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        expectedException.expect(ApiError.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ExceptionEnum.NOT_FOUND.getErrorCode()));

        service.listPasswords(user.getUsername());
    }

    @Test
    public void listPassword_shouldBeError_usernameIsNull() {
        expectedException.expect(ApiError.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ExceptionEnum.BAD_REQUEST.getErrorCode()));

        service.listPasswords(null);
    }

    @Test
    public void deletePassword_shouldDelete() {
        PasswordEntity pe = createPasswordEntity();
        pe.setId("id");

        Mockito.when(passwordRepository.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(pe));

        Boolean result = service.deletePassword(pe.getId());

        assertThat(result).isTrue();
    }

    @Test
    public void deletePassword_ShouldBeError_IdIsNull() {
        expectedException.expect(ApiError.class);
        expectedException.expectMessage(CoreMatchers.equalTo(ExceptionEnum.BAD_REQUEST.getErrorCode()));
        service.deletePassword(null);
    }
}
