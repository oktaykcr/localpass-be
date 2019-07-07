package com.localpass.backend.service;

import com.localpass.backend.common.model.ListResponse;
import com.localpass.backend.model.PasswordEntity;

public interface PasswordService {

    ListResponse listPasswords();

    PasswordEntity addPassword(PasswordEntity passwordEntity);

    PasswordEntity updatePassword(PasswordEntity passwordEntity);

    Boolean deletePassword(Long id);
}
