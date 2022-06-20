package com.example.demo.user;

import java.util.HashMap;
import java.util.Map;

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
    Map<String, String> errors = new HashMap<>();
    if(user.getUsername() == null || user.getUsername().isEmpty()) {
      errors.put("username", "Username cannot be null");
    }
    if(user.getEmail() == null || user.getEmail().isEmpty()) {
      errors.put("email", "E-mail cannot be null");
    }
    if(errors.size() > 0) {
      return ResponseEntity.badRequest().body(errors);
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
