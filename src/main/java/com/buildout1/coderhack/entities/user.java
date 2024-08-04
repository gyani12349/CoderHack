package com.buildout1.coderhack.entities;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.buildout1.coderhack.enums.badge;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

// @Entity
// @Table(name = "user")
@Document(collection = "user")
public class user {
    @Id
    @Column(name = "id")
    private Long id;


    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "score")
    private int score;

    @Column(name = "badge")
    private Set<badge> badge; 

    public user() {
        score=0;
        badge=new HashSet<>();
    }

    public user(Long id, String name,String email,int score, Set<badge> badge) {
       
        this.id=id;
        this.name = name;
        this.score=score;
        this.badge = badge;
        this.email=email;
    }

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<badge> getbadge() {
        return badge;
    }

    public void setbadge(Set<badge> badge) {
        this.badge = badge;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", badge=" + badge +
                ", score=" + score +
                '}';
    }
}