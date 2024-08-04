package com.buildout1.coderhack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import com.buildout1.coderhack.entities.user;
import com.buildout1.coderhack.exceptions.emailAlreadyExists;
import com.buildout1.coderhack.exceptions.invalidArg;
import com.buildout1.coderhack.exceptions.unexpectedBehaviour;
import com.buildout1.coderhack.exceptions.userNotFound;
import com.buildout1.coderhack.service.userService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class userController {


    @Autowired
    private userService userService;
    
     @PostMapping("/user")
    public ResponseEntity<?> createuser(@Validated @RequestBody user user) {
        if (user.getId()==null||user.getName() == null || user.getEmail() == null){
            return new ResponseEntity<>("Please Provide Important Information", HttpStatus.BAD_REQUEST);
        }
       
        try {
            user saveduser = userService.saveuser(user);
            return new ResponseEntity<>(saveduser, HttpStatus.OK);
        } catch (invalidArg e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (emailAlreadyExists e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //update the user
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateuser(@RequestParam int score, @PathVariable Long id) {
        try {
            user saveduser = userService.updateuser(score, id);
            return new ResponseEntity<>(saveduser, HttpStatus.OK);
        } catch (userNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (invalidArg e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(unexpectedBehaviour e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getuser(@RequestParam(defaultValue = "ASC") String sortOrder) {
        try {
            List<user> user = userService.getuser(sortOrder.toLowerCase());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (userNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch(unexpectedBehaviour e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getuserById(@PathVariable Long id) {
        try {
            user user = userService.getuserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (userNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch(unexpectedBehaviour e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteuser(@PathVariable Long id) {
        try {
            userService.deleteuser(id);
            return new ResponseEntity<>("user Deleted Successfully", HttpStatus.OK);
        } catch (userNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch(unexpectedBehaviour e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
}