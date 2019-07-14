package com.localpass.backend.service;

import com.localpass.backend.common.model.ListResponse;
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
        listResponse.setData(passwordList);
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

        PasswordEntity savedPassword = passwordRepository.save(passwordEntity);

        return savedPassword;
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

        if(StringUtils.isNotEmpty(passwordEntity.getName())) {
            updatedPassword.setName(passwordEntity.getName());
        }

        if(StringUtils.isNotEmpty(passwordEntity.getEmail())) {
            updatedPassword.setEmail(passwordEntity.getEmail());
        }

        if(StringUtils.isNotEmpty(passwordEntity.getUsername())) {
            updatedPassword.setUsername(passwordEntity.getUsername());
        }

        if(StringUtils.isNotEmpty(passwordEntity.getPassword())) {
            updatedPassword.setPassword(passwordEntity.getPassword());
        }

        if(StringUtils.isNotEmpty(passwordEntity.getDescription())) {
            updatedPassword.setDescription(passwordEntity.getDescription());
        }

        return passwordRepository.save(updatedPassword);
    }

    @Override
    public Boolean deletePassword(Long id) {
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
}
