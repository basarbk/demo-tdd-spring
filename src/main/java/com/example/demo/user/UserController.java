package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
  void createUser(@RequestBody User user){
    userService.save(user);
    
  }

  // @ExceptionHandler(EmailSendException.class)
  // @ResponseStatus(HttpStatus.BAD_GATEWAY)
  // Map<String, String> handleEmailException(){
  //   return Collections.singletonMap("message", "E-mail send failure");
  // }
  
}
