package com.example.demo.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Autowired
  JavaMailSender mailSender;

  public void sendActivationEmail(String email, String activationToken) {

    try {
      String activationUrl = "http://localhost:3000/activate/" + activationToken;
      SimpleMailMessage message = new SimpleMailMessage(); 
      message.setFrom("edd.welch5@ethereal.email");
      message.setTo(email); 
      message.setSubject("Account activation"); 
      message.setText(activationUrl);
      mailSender.send(message);
    } catch (Exception e) {
      e.printStackTrace();
      throw new EmailSendException();
    }
  }
  
}
