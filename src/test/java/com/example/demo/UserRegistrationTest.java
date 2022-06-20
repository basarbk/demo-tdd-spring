package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.demo.email.EmailSendException;
import com.example.demo.email.EmailService;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationTest {

  @Autowired
  TestRestTemplate testRestTemplate;

  @Autowired
  UserRepository userRepository;

  @MockBean
  EmailService emailService;

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

  @Test
  public void postUser_whenUserIsValid_sendsActivationEmail(){
    doNothing().when(emailService).sendActivationEmail(anyString(), anyString());
    testRestTemplate.postForEntity("/users", createValidUser(), Object.class);
    verify(emailService, times(1)).sendActivationEmail(anyString(), anyString());
  }

  @Test
  public void postUser_whenActivationEmailFails_returns502(){
    doThrow(EmailSendException.class).when(emailService).sendActivationEmail(anyString(), anyString());
    ResponseEntity<Object> response = testRestTemplate.postForEntity("/users", createValidUser(), Object.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
  }

  @Test
  public void postUser_whenActivationEmailFails_dontSaveUser(){
    doThrow(EmailSendException.class).when(emailService).sendActivationEmail(anyString(), anyString());
    testRestTemplate.postForEntity("/users", createValidUser(), Object.class);
    assertThat(userRepository.count()).isEqualTo(0);
  }

  @Test
  public void postUser_whenUsernameIsNotValid_returns400(){
    User user = createValidUser();
    user.setUsername(null);
    ResponseEntity<Object> response = testRestTemplate.postForEntity("/users", user, Object.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void postUser_whenUsernameIsNotValid_returnsErrorMessage(){
    User user = createValidUser();
    user.setUsername(null);
    ResponseEntity<Object> response = testRestTemplate.postForEntity("/users", user, Object.class);
    assertThat(response.getBody().toString()).contains("Username cannot be null");
  }

  private User createValidUser(){
    User user = new User();
    user.setUsername("user1");
    user.setEmail("user@mail.com");
    user.setPassword("P4ssword");
    return user;
  }
}
