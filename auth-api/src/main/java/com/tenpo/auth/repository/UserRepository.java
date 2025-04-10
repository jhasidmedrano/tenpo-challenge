package com.tenpo.auth.repository;

import com.tenpo.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);

    User findUserByUsername(String username);
}
