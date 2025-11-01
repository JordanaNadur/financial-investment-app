package com.financial.auth.domain.port;

import com.financial.auth.domain.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User save(User user);
}
