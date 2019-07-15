package com.localpass.backend.repository;

import com.localpass.backend.model.password.PasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<PasswordEntity, String> {
    List<PasswordEntity> findByUserId(String userId);
    PasswordEntity findByIdAndUserId(String id, Long userId);
}
