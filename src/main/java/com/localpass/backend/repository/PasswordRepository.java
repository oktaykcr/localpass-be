package com.localpass.backend.repository;

import com.localpass.backend.model.password.PasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<PasswordEntity, Long> {
    List<PasswordEntity> findByUserId(Long userId);
    PasswordEntity findByIdAndUserId(Long id, Long userId);
}
