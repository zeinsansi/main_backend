package com.backend.main._backend.repository;

import com.backend.main._backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String> {
    User findByemail(String email);
    User findUserByUsername(String username);
}
