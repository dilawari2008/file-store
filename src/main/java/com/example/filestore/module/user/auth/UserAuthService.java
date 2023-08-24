package com.example.filestore.module.user.auth;

import com.example.filestore.module.user.User;
import com.example.filestore.module.user.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import java.util.UUID;

@Service
public class UserAuthService {
    @Autowired
    private UserRepository userRepository;

    public String login(String username, String password) {
        Optional user = userRepository.login(username,encryptPass(password));
        if(user.isPresent()){
            String token = UUID.randomUUID().toString();
            User user1= (User) user.get();
            user1.setToken(token);
            userRepository.save(user1);
            return token;
        }

        return StringUtils.EMPTY;
    }

    @Transactional
    public String register(String username, String password, String name) {
        User user = new User(username, encryptPass(password), name);
        User user1 = userRepository.getUserByUsername(username);
        if(user1 != null) throw new BadCredentialsException("username has to be different");
        userRepository.save(user);
        return login(username, password);
    }

    public Optional findByToken(String token) {
        Optional user= userRepository.findByToken(token);
        if(user.isPresent()){
            User user1 = (User) user.get();
            return Optional.of(user1);
        }
        return  Optional.empty();
    }

    private String encryptPass(String password) {
        String encryptedpassword = null;
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            encryptedpassword = s.toString();
            return encryptedpassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
