package com.localpass.backend.repository;

import com.localpass.backend.model.password.PasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<PasswordEntity, Long> {
}
