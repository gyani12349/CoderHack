package com.buildout1.coderhack.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.buildout1.coderhack.enums.badge;
import com.buildout1.coderhack.enums.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class user {
  @Id
  private String id;

  @Indexed
  private String username;

  private int score = 0;

  private status status;

  private Set<badge> badges;
}
