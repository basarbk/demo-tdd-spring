package com.example.demo.user;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.email.EmailSendException;
import com.example.demo.email.EmailService;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  EmailService emailService;

  @Transactional(rollbackOn = EmailSendException.class)
  public void save(User user) {
    user.setActivationToken(UUID.randomUUID().toString());
    userRepository.save(user);
    emailService.sendActivationEmail(user.getEmail(), user.getActivationToken());
  }

  public void activate(String token) {
    userRepository.findByActivationToken(token).orElseThrow(() -> new InvalidTokenException());
  }
  
}
