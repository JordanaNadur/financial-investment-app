package com.financial.auth.domain.usecase;

import com.financial.auth.domain.User;
import com.financial.auth.domain.port.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(String username, String email, String password, User.Role role) {
        User user = new User(username, email, passwordEncoder.encode(password), role);
        return userRepository.save(user);
    }
}
