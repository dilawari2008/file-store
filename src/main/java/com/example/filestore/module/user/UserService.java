package com.example.filestore.module.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public ResponseEntity<User> addUser(@RequestBody User user){
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
