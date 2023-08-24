package com.example.filestore.module.user.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/login")
    public String getToken(@RequestParam("username") final String username, @RequestParam("password") final String password){
        String token= userAuthService.login(username,password);
        if(StringUtils.isEmpty(token)){
            return "user not found, create user";
        }
        return token;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestParam("username") final String username, @RequestParam("password") final String password, @RequestParam String name){
        try{
            String token = userAuthService.register(username, password, name);
            if (StringUtils.isEmpty(token)) {
                return new ResponseEntity("no token found, retry with another user", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(token, HttpStatus.OK);
        }catch (RuntimeException e){
            if(e.getMessage() == "username has to be different"){
                return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }
}
