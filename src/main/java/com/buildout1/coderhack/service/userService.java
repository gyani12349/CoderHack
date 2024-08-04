package com.buildout1.coderhack.service;

import org.springframework.web.server.ResponseStatusException;

import com.buildout1.coderhack.dto.regUser;
import com.buildout1.coderhack.entities.user;

import java.util.List;

public interface userService {
  // Registers a new user to the contest and save in the repository if not already registered
  public boolean register(regUser registerUserDTO) throws ResponseStatusException;

  // Deregisters a specific user (update registration status to cancelled) from the contest if exist
  public boolean cancelRegistration(String userId) throws ResponseStatusException;

  // Retrieves a list of all registered users
  public List<user> getAllRegisteredUsers();

  // Retrieves the details of a specific user
  public user getUserById(String userId) throws ResponseStatusException;

  // Updates the score of a specific user
  public boolean updateUserScore(String userId, int newScore) throws ResponseStatusException;
}
