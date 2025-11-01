package com.financial.auth.infrastructure.adapter;

import com.financial.auth.domain.User;
import com.financial.auth.domain.port.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryAdapter extends JpaRepository<User, Long>, UserRepository {

    @Override
    Optional<User> findByUsername(String username);

    @Override
    Optional<User> findByEmail(String email);
}
