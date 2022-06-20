package com.example.demo.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  UserRepository userRepository;

  @PostMapping("/users")
  void createUser(@RequestBody User user){
    user.setActivationToken(UUID.randomUUID().toString());
    userRepository.save(user);
  }
  
}
