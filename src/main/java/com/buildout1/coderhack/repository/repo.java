package com.buildout1.coderhack.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.buildout1.coderhack.entities.user;
import com.buildout1.coderhack.enums.status;


@Repository
public interface repo extends  MongoRepository<user, Long>{
    
    public user findByEmail(String email);
    List<user> findAllById(Long id, Sort sort);

}
