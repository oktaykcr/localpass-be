package com.localpass.backend.service;

import com.localpass.backend.common.model.ListResponse;
import com.localpass.backend.exception.ExceptionEnum;
import com.localpass.backend.exception.ExceptionFactory;
import com.localpass.backend.model.password.PasswordEntity;
import com.localpass.backend.repository.PasswordRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasswordServiceImpl implements PasswordService  {

    private PasswordRepository repository;

    @Autowired
    public PasswordServiceImpl(PasswordRepository repository) {
        this.repository = repository;
    }

    @Override
    public ListResponse listPasswords() {
        ListResponse listResponse = new ListResponse();
        List<PasswordEntity> passwordList = repository.findAll();
        listResponse.setData(passwordList);
        listResponse.setTotalCount(passwordList.size());

        return listResponse;
    }

    @Override
    public PasswordEntity addPassword(PasswordEntity passwordEntity) {

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

        PasswordEntity savedPassword = repository.save(passwordEntity);

        return savedPassword;
    }

    @Override
    public PasswordEntity updatePassword(PasswordEntity passwordEntity) {

        if(passwordEntity.getId() == null) {
            throw ExceptionFactory.getApiError(ExceptionEnum.BAD_REQUEST, "id");
        }

        PasswordEntity updatedPassword = repository.getOne(passwordEntity.getId());

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

        return repository.save(updatedPassword);
    }

    @Override
    public Boolean deletePassword(Long id) {
        if(id == null) {
            throw ExceptionFactory.getApiError(ExceptionEnum.BAD_REQUEST, "id");
        }
        Optional<PasswordEntity> passwordEntity = repository.findById(id);

        if(!passwordEntity.isPresent()) {
            throw ExceptionFactory.getApiError(ExceptionEnum.BAD_REQUEST, "password");
        }

        repository.deleteById(id);
        return true;
    }
}
