package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

@DataJpaTest
public class UserRepositoryTest {

  @Autowired
	TestEntityManager testEntityManager;
	
	@Autowired
	UserRepository userRepository;
	
  @Test
	public void findByActivationToken_whenTokenExists_returnsUser() {
		testEntityManager.persist(createValidUser());
		
		Optional<User> inDB = userRepository.findByActivationToken("abcd1234");
		assertThat(inDB.isPresent()).isTrue();
		
	}
	
	@Test
	public void findByActivationToken_whenTokenDoesNotExist_returnsNull() {
		Optional<User> inDB = userRepository.findByActivationToken("nonexistinguser");
		assertThat(inDB.isPresent()).isFalse();
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
