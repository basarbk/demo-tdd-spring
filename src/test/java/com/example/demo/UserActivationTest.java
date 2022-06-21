package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserActivationTest {

  @Autowired
  TestRestTemplate testRestTemplate;

  @Autowired
  UserRepository userRepository;

  @Test
  public void postToken_whenItsInvalid_returnsBadRequest(){
    ResponseEntity<Object> response = testRestTemplate.postForEntity("/users/token/123", null, Object.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void postToken_whenItsValid_returnsSuccess(){
    userRepository.save(createValidUser());
    ResponseEntity<Object> response = testRestTemplate.postForEntity("/users/token/abcd1234", null, Object.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  private User createValidUser(){
    User user = new User();
    user.setUsername("user1");
    user.setEmail("user@mail.com");
    user.setPassword("P4ssword");
    user.setActivationToken("abcd1234");
    return user;
  }
  
}
