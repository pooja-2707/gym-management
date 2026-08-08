package com.gym.management.service;

import com.gym.management.entity.Admin;
import com.gym.management.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

/*
 * This service implements Spring Security's UserDetailsService interface.
 * Spring Security calls loadUserByUsername() during login to verify credentials.
 */
@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    /*
     * Spring Security calls this method during login.
     * It fetches the admin from DB and wraps it in a UserDetails object.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + username));

        return new User(
                admin.getUsername(),
                admin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    public void registerAdmin(String username, String email, String password) {
        if (adminRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken!");
        }
        if (email != null && !email.trim().isEmpty() && adminRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered!");
        }

        Admin admin = new Admin();
        admin.setUsername(username.trim());
        admin.setEmail(email != null ? email.trim() : null);
        admin.setPassword(passwordEncoder.encode(password));

        adminRepository.save(admin);
    }
}
