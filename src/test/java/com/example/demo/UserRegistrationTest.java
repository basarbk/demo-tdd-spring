package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationTest {

  @Autowired
  TestRestTemplate testRestTemplate;

  @Autowired
  UserRepository userRepository;

  @AfterEach
  public void cleanup(){
    userRepository.deleteAll();
  }
  
  @Test
  public void postUser_whenUserIsValid_returns200(){
    ResponseEntity<Object> response = testRestTemplate.postForEntity("/users", createValidUser(), Object.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void postUser_whenUserIsValid_savesUserToDB(){
    testRestTemplate.postForEntity("/users", createValidUser(), Object.class);
    assertThat(userRepository.count()).isEqualTo(1);
  }

  @Test
  public void postUser_whenUserIsValid_saveUserInactiveModeWithActivationToken(){
    testRestTemplate.postForEntity("/users", createValidUser(), Object.class);
    User inDB = userRepository.findAll().get(0);
    assertThat(inDB.isActive()).isEqualTo(false);
    assertThat(inDB.getActivationToken()).isNotNull();
  }

  private User createValidUser(){
    User user = new User();
    user.setUsername("user1");
    user.setEmail("user@mail.com");
    user.setPassword("P4ssword");
    return user;
  }
}
