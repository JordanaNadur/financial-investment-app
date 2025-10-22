package com.financial.investment.domain.port;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}

class User {
    private String username;
    private String role;

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
}
