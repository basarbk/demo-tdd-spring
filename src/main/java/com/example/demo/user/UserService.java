package com.example.demo.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  public void save(User user) {
    user.setActivationToken(UUID.randomUUID().toString());
    userRepository.save(user);
  }
  
}
