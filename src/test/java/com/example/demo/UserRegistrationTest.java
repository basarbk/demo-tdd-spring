package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.user.User;

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
  
  @Test
  public void postUser_whenUserIsValid_returns200(){
    User user = new User();
    user.setUsername("user1");
    user.setEmail("user@mail.com");
    user.setPassword("P4ssword");
    ResponseEntity<Object> response = testRestTemplate.postForEntity("/users", user, Object.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
