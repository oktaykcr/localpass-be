package com.localpass.backend.service;

import com.localpass.backend.common.model.ListResponse;
import com.localpass.backend.model.password.PasswordEntity;
import com.localpass.backend.model.password.PasswordEntityRequest;

public interface PasswordService {

    ListResponse listPasswords(String username);

    PasswordEntity addPassword(PasswordEntityRequest request);

    PasswordEntity updatePassword(PasswordEntity passwordEntity);

    Boolean deletePassword(String id);
}
