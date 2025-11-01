package com.financial.transaction.infrastructure.adapter;

import com.financial.transaction.domain.User;
import com.financial.transaction.domain.port.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    @Override
    public Optional<User> findByUsername(String username) {
        // Mock implementation - in a real app, this would query the database
        if ("user".equals(username)) {
            return Optional.of(new User(1L, "user", "$2a$10$dummyhashedpassword"));
        }
        return Optional.empty();
    }
}
