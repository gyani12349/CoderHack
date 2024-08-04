package com.buildout1.coderhack.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.never;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.buildout1.coderhack.entities.user;
import com.buildout1.coderhack.service.userService;
import com.buildout1.coderhack.repository.repo;
import com.buildout1.coderhack.enums.badge;
import com.buildout1.coderhack.enums.status;
import com.buildout1.coderhack.exceptions.emailAlreadyExists;
import com.buildout1.coderhack.exceptions.invalidArg;
import com.buildout1.coderhack.exceptions.userNotFound;


@SpringBootTest
public class userServicetest {

    @InjectMocks
    private userService userService;

    @Mock
    private repo repository;

     @Test
    public void saveUserTest() {
        user user = new user();
        user.setId(9999L);
        user.setName("Gyani");
        user.setEmail("gyani@gmail.com");

        when(repository.findByEmail("gyani@gmail.com")).thenReturn(null);
        when(repository.save(user)).thenReturn(user);

        user result = userService.saveuser(user);

        assertEquals(user, result);
        verify(repository, times(1)).findByEmail("gyani@gmail.com");
        verify(repository, times(1)).save(user);
    }

  
    @Test
    public void saveInvUserTest() {
        user user = new user();
        user.setId(9999L);
        user.setName("Cray");
        user.setEmail("cray");

        when(repository.findByEmail("cray")).thenReturn(null);

        assertThrows(invalidArg.class, () -> userService.saveuser(user));

        verify(repository, times(1)).findByEmail("cray");
        verify(repository, never()).save(user);
    }

    
    @Test
    public void saveDupUsertest() {
        user user = new user();
        user.setId(9999L);
        user.setName("Plop");
        user.setEmail("plop@hotmail.com");

        when(repository.findByEmail("plop@hotmail.com")).thenReturn(user);

        assertThrows(emailAlreadyExists.class, () -> userService.saveuser(user));

        verify(repository, times(1)).findByEmail("plop@hotmail.com");
        verify(repository, never()).save(user);
    }


    @Test
    public void validScoreTest() {
        user existinguser = new user();
        existinguser.setId(9999L);
        existinguser.setEmail("mike@gmail.com");
        existinguser.setName("mike");

        int updatedScore = 75;

        when(repository.findById(9999L)).thenReturn(Optional.of(existinguser));
        when(repository.save(existinguser)).thenReturn(existinguser);

        user result = userService.updateuser(updatedScore, 9999L);

        assertEquals(updatedScore, result.getScore());
        verify(repository, times(1)).findById(9999L);
        verify(repository, times(1)).save(existinguser);
    }

    @Test
    public void invalidScoreTest() {
        user existinguser = new user();
        existinguser.setId(9999L);
        existinguser.setScore(100);

        // Wrong score
        int updatedScore = 200; 

        when(repository.findById(9999L)).thenReturn(Optional.of(existinguser));

        assertThrows(invalidArg.class, () -> userService.updateuser(updatedScore, 9999L));

        verify(repository, times(1)).findById(9999L);
        verify(repository, never()).save(existinguser);
    }



    @Test
    public void getUserTest() {
        Set<badge> badges = new HashSet<>();
        badges.add(badge.CODE_NINJA);
        badges.add(badge.CODE_CHAMP);
        List<user> users = new ArrayList<>();
        users.add(new user(9999L, "user1", "firstyser@example.com", 100,badges));
        users.add(new user(1111L, "user2", "seconduser@example.com", 120,badges));

        when(repository.findAll(Sort.by(Sort.Direction.ASC, "score"))).thenReturn(users);

        List<user> result = userService.getuser("ASC");

        assertEquals(users.size(), result.size());
        assertEquals(users.get(0), result.get(0));
        assertEquals(users.get(1), result.get(1));
        verify(repository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "score"));
    }

    @Test
    public void deleteUserTest() {
        user user = new user();
        user.setId(9999L);
        user.setName("Test");
        user.setEmail("test@example.com");

        when(repository.findById(9999L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteuser(9999L));

        verify(repository, times(1)).findById(9999L);
        verify(repository, times(1)).deleteById(9999L);
    }


    @Test
    public void userNotFoundTest() {
        when(repository.findById(9999L)).thenReturn(Optional.empty());

        assertThrows(userNotFound.class, () -> userService.updateuser(100, 9999L));

        verify(repository, times(1)).findById(9999L);
        verify(repository, never()).save(any());
    }
    
}