package com.enershare.user.service;

import com.enershare.user.model.User;
import com.enershare.user.model.Role;
import com.enershare.user.repository.UserRepository;
import com.enershare.user.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        // Assume roles are passed as Set<String> and mapped to Role entities elsewhere
        // For simplicity, not handling roles mapping here
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserRequest userRequest) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        u.setUsername(userRequest.getUsername());
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        // Assume roles are passed as Set<String> and mapped to Role entities elsewhere
        // For simplicity, not handling roles mapping here
        return userRepository.save(u);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User setUserEnabled(Long id, boolean enabled) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        return userRepository.save(user);
    }
}
