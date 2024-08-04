package com.buildout1.coderhack.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.buildout1.coderhack.entities.user;
import com.buildout1.coderhack.enums.status;

import java.util.List;

public interface repo extends MongoRepository<user, String> {
  public boolean existsById(String id);

  public boolean existsByUsername(String username);

  public user findByUsername(String username);

  List<user> findBystatusOrderByScoreDesc(status status);
}
