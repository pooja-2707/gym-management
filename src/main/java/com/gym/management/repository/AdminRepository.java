package com.gym.management.repository;

import com.gym.management.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Find admin by username (used for login)
    Optional<Admin> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
