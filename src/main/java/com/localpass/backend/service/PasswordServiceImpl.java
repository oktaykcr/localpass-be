package com.localpass.backend.service;

import com.localpass.backend.common.model.ListResponse;
import com.localpass.backend.common.util.AES;
import com.localpass.backend.exception.ExceptionEnum;
import com.localpass.backend.exception.ExceptionFactory;
import com.localpass.backend.model.password.PasswordEntity;
import com.localpass.backend.model.password.PasswordEntityRequest;
import com.localpass.backend.model.user.User;
import com.localpass.backend.repository.PasswordRepository;
import com.localpass.backend.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PasswordServiceImpl implements PasswordService  {

    private PasswordRepository passwordRepository;
    private UserRepository userRepository;

    @Autowired
    public PasswordServiceImpl(PasswordRepository passwordRepository, UserRepository userRepository) {
        this.passwordRepository = passwordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ListResponse listPasswords(String username) {

        if(StringUtils.isEmpty(username)) {
            throw ExceptionFactory.getApiError(ExceptionEnum.BAD_REQUEST, "username");
        }

        User user = userRepository.findByUsername(username);

        if(user == null) {
            throw ExceptionFactory.getApiError(ExceptionEnum.NOT_FOUND, "user");
        }

        ListResponse listResponse = new ListResponse();
        List<PasswordEntity> passwordList = passwordRepository.findByUserId(user.getId());
        List<PasswordEntity> decryptedPasswordList = new ArrayList<>();
        for(PasswordEntity entity : passwordList) {
            decryptedPasswordList.add(decryptPasswordEntity(entity, username));
        }


        listResponse.setData(decryptedPasswordList);
        listResponse.setTotalCount(passwordList.size());

        return listResponse;
    }

    @Override
    public PasswordEntity addPassword(PasswordEntityRequest request) {

        PasswordEntity passwordEntity = request.getPasswordEntity();

        if(StringUtils.isEmpty(passwordEntity.getName())) {
            throw ExceptionFactory.getApiError(ExceptionEnum.BAD_REQUEST, "name");
        }

        if(StringUtils.isEmpty(passwordEntity.getUsername())) {
            throw ExceptionFactory.getApiError(ExceptionEnum.BAD_REQUEST, "username");
        }

        if(StringUtils.isEmpty(passwordEntity.getPassword())) {
            throw ExceptionFactory.getApiError(ExceptionEnum.BAD_REQUEST, "password");
        }

        if(StringUtils.isEmpty(passwordEntity.getEmail())) {
            throw ExceptionFactory.getApiError(ExceptionEnum.BAD_REQUEST, "email");
        }

        User user = userRepository.findByUsername(request.getUsername());
        if(user == null) {
            throw ExceptionFactory.getApiError(ExceptionEnum.NOT_FOUND, "user");
        }
        passwordEntity.setUser(user);

        PasswordEntity encryptedPasswordEntity = encryptPasswordEntity(passwordEntity, request.getUsername());

        return passwordRepository.save(encryptedPasswordEntity);
    }

    @Override
    public PasswordEntity updatePassword(PasswordEntity passwordEntity) {

        if(passwordEntity.getId() == null) {
            throw ExceptionFactory.getApiError(ExceptionEnum.BAD_REQUEST, "id");
        }

        PasswordEntity updatedPassword = passwordRepository.getOne(passwordEntity.getId());

        if(updatedPassword == null) {
            throw ExceptionFactory.getApiError(ExceptionEnum.NOT_FOUND, "password");
        }

        String username = updatedPassword.getUser().getUsername();
        String secret = username + "_secret";

        if(StringUtils.isNotEmpty(passwordEntity.getName())) {
            updatedPassword.setName(AES.encrypt(passwordEntity.getName(), secret));
        }

        if(StringUtils.isNotEmpty(passwordEntity.getEmail())) {
            updatedPassword.setEmail(AES.encrypt(passwordEntity.getEmail(), secret));
        }

        if(StringUtils.isNotEmpty(passwordEntity.getUsername())) {
            updatedPassword.setUsername(AES.encrypt(passwordEntity.getUsername(), secret));
        }

        if(StringUtils.isNotEmpty(passwordEntity.getPassword())) {
            updatedPassword.setPassword(AES.encrypt(passwordEntity.getPassword(), secret));
        }

        if(StringUtils.isNotEmpty(passwordEntity.getDescription())) {
            updatedPassword.setDescription(AES.encrypt(passwordEntity.getDescription(), secret));
        }

        return passwordRepository.save(updatedPassword);
    }

    @Override
    public Boolean deletePassword(String id) {
        if(id == null) {
            throw ExceptionFactory.getApiError(ExceptionEnum.BAD_REQUEST, "id");
        }
        Optional<PasswordEntity> passwordEntity = passwordRepository.findById(id);

        if(!passwordEntity.isPresent()) {
            throw ExceptionFactory.getApiError(ExceptionEnum.NOT_FOUND, "password");
        }

        passwordRepository.deleteById(id);
        return true;
    }

    private PasswordEntity encryptPasswordEntity(PasswordEntity passwordEntity, String username) {
        String secret = username + "_secret";
        PasswordEntity encryptedPasswordEntity = new PasswordEntity();
        encryptedPasswordEntity.setName(AES.encrypt(passwordEntity.getName(), secret));
        encryptedPasswordEntity.setUsername(AES.encrypt(passwordEntity.getUsername(), secret));
        encryptedPasswordEntity.setPassword(AES.encrypt(passwordEntity.getPassword(), secret));
        encryptedPasswordEntity.setEmail(AES.encrypt(passwordEntity.getEmail(), secret));
        encryptedPasswordEntity.setDescription(AES.encrypt(passwordEntity.getDescription(), secret));
        encryptedPasswordEntity.setUser(passwordEntity.getUser());

        return encryptedPasswordEntity;
    }

    private PasswordEntity decryptPasswordEntity(PasswordEntity passwordEntity, String username) {
        String secret = username + "_secret";
        PasswordEntity decryptedPasswordEntity = new PasswordEntity();
        decryptedPasswordEntity.setName(AES.decrypt(passwordEntity.getName(), secret));
        decryptedPasswordEntity.setUsername(AES.decrypt(passwordEntity.getUsername(), secret));
        decryptedPasswordEntity.setPassword(AES.decrypt(passwordEntity.getPassword(), secret));
        decryptedPasswordEntity.setEmail(AES.decrypt(passwordEntity.getEmail(), secret));
        decryptedPasswordEntity.setDescription(AES.decrypt(passwordEntity.getDescription(), secret));
        decryptedPasswordEntity.setUser(passwordEntity.getUser());
        decryptedPasswordEntity.setId(passwordEntity.getId());

        return decryptedPasswordEntity;
    }
}
