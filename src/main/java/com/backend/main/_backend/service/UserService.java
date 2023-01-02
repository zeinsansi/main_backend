package com.backend.main._backend.service;

import com.backend.main._backend.model.User;
import com.backend.main._backend.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User saveUser(User user){
        return userRepo.save(user);
    }

    public User getUserById(String id){
        return userRepo.findById(id).orElse(null);
    }

    public User getUserByemail(String email){
        User user = userRepo.findByemail(email);
        if (user != null){
            return user;
        }
        return null;
    }
}
