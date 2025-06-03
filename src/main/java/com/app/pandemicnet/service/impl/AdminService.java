package com.app.pandemicnet.service.impl;

import com.app.pandemicnet.model.User;
import com.app.pandemicnet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setUniqueId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @Transactional
    public User updateUser(Long id, User updated) {
        User user = getUser(id);
        user.setPhoneNumber(updated.getPhoneNumber());
        user.setEmail(updated.getEmail());
        if (updated.getPasswordHash() != null) {
            user.setPasswordHash(passwordEncoder.encode(updated.getPasswordHash()));
        }
        user.setRole(updated.getRole());
        user.setStatus(updated.getStatus());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}