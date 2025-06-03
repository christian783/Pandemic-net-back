package com.app.pandemicnet.service.impl;//package com.app.pandemicnet.service.impl;

import com.app.pandemicnet.dto.RegisterRequest;
import com.app.pandemicnet.model.User;
import com.app.pandemicnet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(User.Role.valueOf(String.valueOf(userDTO.getRole())));
        return userRepository.save(user);
    }

    public User findByEmail(String phoneNumber) {
        return userRepository.findUserByEmail(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User updateStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User.Status newStatus = User.Status.valueOf(status);
        user.setStatus(newStatus);
        user = userRepository.save(user);
        if (newStatus == User.Status.AT_RISK || newStatus == User.Status.INFECTED) {
            notificationService.sendNotification(id,
                    "Your status has been updated to " + status + ". Please take appropriate action.");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles(String.valueOf(user.getRole()))
                .build();
    }
}
