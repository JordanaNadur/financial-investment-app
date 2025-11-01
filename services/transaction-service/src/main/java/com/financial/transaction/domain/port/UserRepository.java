package com.financial.transaction.domain.port;

import com.financial.transaction.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}
