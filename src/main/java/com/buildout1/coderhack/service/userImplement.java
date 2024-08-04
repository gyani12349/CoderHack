package com.buildout1.coderhack.service;

import java.util.List;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.buildout1.coderhack.dto.regUser;
import com.buildout1.coderhack.entities.user;
import com.buildout1.coderhack.repository.repo;
import com.buildout1.coderhack.enums.badge;
import com.buildout1.coderhack.enums.status;

@Service
public class userImplement implements userService {
  @Autowired
  private repo repository;

  @Override
  public boolean register(regUser registerUserDTO) throws ResponseStatusException {
    String username = registerUserDTO.getUsername();
    user user = repository.findByUsername(username);

    if (user != null) {
      if (user.getStatus() == status.REGISTERED) {
        throw new ResponseStatusException(HttpStatus.CONFLICT);
      } else {
        user.setStatus(status.REGISTERED);

        repository.save(user);
      }

    } else {
      user newUser = new user();
      newUser.setUsername(username);
      newUser.setScore(0);
      newUser.setStatus(status.REGISTERED);
      newUser.setBadges(new HashSet<>());

      repository.save(newUser);
    }

    return true;
  }

  @Override
  public boolean cancelRegistration(String userId) throws ResponseStatusException {
    user registeredUser = repository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    registeredUser.setStatus(status.CANCELLED);
    registeredUser.setBadges(new HashSet<>());
    registeredUser.setScore(0);

    repository.save(registeredUser);

    return true;
  }
  
  @Override
  public List<user> getAllRegisteredUsers() {
    return repository.findBystatusOrderByScoreDesc(status.REGISTERED);
  }

  @Override
  public user getUserById(String userId) throws ResponseStatusException {
    user registeredUser = repository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (registeredUser.getStatus() == status.CANCELLED) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    return registeredUser;
  }

  @Override
  public boolean updateUserScore(String userId, int newScore) throws ResponseStatusException {
    user registeredUser = repository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (registeredUser.getStatus() == status.CANCELLED ||
        newScore > 100 ||
        newScore < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    registeredUser.setScore(newScore);
    addBadge(registeredUser, newScore);

    repository.save(registeredUser);

    return true;
  }

  private void addBadge(user user, int score) {
    
    if (score >= 1 && score < 30) {
      user.getBadges().add(badge.CODE_NINJA);
    } else if (score >= 30 && score < 60) {
      user.getBadges().add(badge.CODE_CHAMP);
    } else {
      user.getBadges().add(badge.CODE_MASTER);
    }
  }

}
