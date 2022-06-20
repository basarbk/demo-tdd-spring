package com.example.demo.user;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  UserService userService;

  @PostMapping("/users/token/{token}")
  void activateUser(@PathVariable String token){
    userService.activate(token);
  }

  @PostMapping("/users")
  ResponseEntity<?> createUser(@RequestBody User user){
    if(user.getUsername() == null || user.getUsername().isEmpty()) {
      return ResponseEntity.status(400).body(Collections.singletonMap("username", "Username cannot be null"));
    }
    userService.save(user);
    return ResponseEntity.ok().build();
  }

  // @ExceptionHandler(EmailSendException.class)
  // @ResponseStatus(HttpStatus.BAD_GATEWAY)
  // Map<String, String> handleEmailException(){
  //   return Collections.singletonMap("message", "E-mail send failure");
  // }
  
}
