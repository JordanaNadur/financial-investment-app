package com.financial.auth.infrastructure.config;

import com.financial.auth.domain.port.UserRepository;
import com.financial.auth.domain.usecase.AuthenticateUserUseCase;
import com.financial.auth.domain.usecase.RegisterUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new AuthenticateUserUseCase(userRepository, passwordEncoder);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new RegisterUserUseCase(userRepository, passwordEncoder);
    }
}
