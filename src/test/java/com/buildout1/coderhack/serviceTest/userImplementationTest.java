package com.buildout1.coderhack.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import com.buildout1.coderhack.dto.regUser;
import com.buildout1.coderhack.entities.user;
import com.buildout1.coderhack.repository.repo;
import com.buildout1.coderhack.service.userImplement;
import com.buildout1.coderhack.enums.badge;
import com.buildout1.coderhack.enums.status;


public class userImplementationTest {
  @Mock
  private repo userRepository;

  @InjectMocks
  private userImplement userServices;

  @BeforeEach
  public void setup() {
    userServices = new userImplement();
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testRegister_Success() {
    regUser registerUserDTO = new regUser("testUser");
    when(userRepository.existsByUsername("testUser")).thenReturn(false);

    assertTrue(userServices.register(registerUserDTO));

    verify(userRepository, times(1)).save(any(user.class));
  }


  @Test
  public void testCancelRegistration_Success() {
    user userEntity = new user();
    userEntity.setId("1");
    userEntity.setUsername("testUser");
    userEntity.setStatus(status.REGISTERED);

    when(userRepository.findById("1")).thenReturn(java.util.Optional.of(userEntity));

    assertTrue(userServices.cancelRegistration("1"));
    assertEquals(status.CANCELLED, userEntity.getStatus());
    verify(userRepository, times(1)).save(userEntity);
  }

  @Test
  public void testGetAllRegisteredUsers() {
    List<user> userList = new ArrayList<>();
    user user1 = new user("1", "user1", 50, status.REGISTERED, new HashSet<>());
    user user2 = new user("2", "user2", 30, status.REGISTERED, new HashSet<>());
    userList.add(user1);
    userList.add(user2);

    when(userRepository.findBystatusOrderByScoreDesc(status.REGISTERED))
        .thenReturn(userList);

    List<user> result = userServices.getAllRegisteredUsers();

    assertEquals(2, result.size());
    assertEquals("user1", result.get(0).getUsername());
    assertEquals("user2", result.get(1).getUsername());
  }

  @Test
  public void testUpdateUserScore_Success() {
    user userEntity = new user();
    userEntity.setId("1");
    userEntity.setUsername("testUser");
    userEntity.setStatus(status.REGISTERED);
    userEntity.setScore(50);
    userEntity.setBadges(new HashSet<>());

    when(userRepository.findById("1")).thenReturn(java.util.Optional.of(userEntity));

    assertTrue(userServices.updateUserScore("1", 70));

    assertEquals(70, userEntity.getScore());
    assertTrue(userEntity.getBadges().contains(badge.CODE_MASTER));
    verify(userRepository, times(1)).save(userEntity);
  }
}
