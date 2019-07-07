package com.localpass.backend.service;

import com.localpass.backend.common.model.ListResponse;
import com.localpass.backend.model.PasswordEntity;
import com.localpass.backend.repository.PasswordRepository;
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

        if(passwordEntity.getName() == null) {
            throw new NullPointerException();
        }

        if(passwordEntity.getUsername() == null) {
            throw new NullPointerException();
        }

        if(passwordEntity.getPassword() == null) {
            throw new NullPointerException();
        }

        if(passwordEntity.getEmail() == null) {
            throw new NullPointerException();
        }

        PasswordEntity savedPassword = repository.save(passwordEntity);

        return savedPassword;
    }

    @Override
    public PasswordEntity updatePassword(PasswordEntity passwordEntity) {

        if(passwordEntity == null) {
            // BAD REQUEST
            throw new NullPointerException();
        }

        if(passwordEntity.getId() == null) {
            // BAD REQUEST
            throw new NullPointerException();
        }

        PasswordEntity updatedPassword = repository.getOne(passwordEntity.getId());

        if(updatedPassword == null) {
            // NOT FOUND
            throw new NullPointerException();
        }

        if(passwordEntity.getName() != null) {
            updatedPassword.setName(passwordEntity.getName());
        }

        if(passwordEntity.getEmail() != null) {
            updatedPassword.setEmail(passwordEntity.getEmail());
        }

        if(passwordEntity.getUsername() != null) {
            updatedPassword.setUsername(passwordEntity.getUsername());
        }

        if(passwordEntity.getPassword() != null) {
            updatedPassword.setPassword(passwordEntity.getPassword());
        }

        if(passwordEntity.getDescription() != null) {
            updatedPassword.setDescription(passwordEntity.getDescription());
        }

        return repository.save(updatedPassword);
    }

    @Override
    public Boolean deletePassword(Long id) {

        Optional<PasswordEntity> foundPasswordEntity = repository.findById(id);
        if(!foundPasswordEntity.isPresent()) {
            return false;
        }
        repository.delete(foundPasswordEntity.get());
        return true;
    }
}
