package com.buildout1.coderhack.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.buildout1.coderhack.entities.user;
import com.buildout1.coderhack.repository.repo;
import com.buildout1.coderhack.enums.badge;
import com.buildout1.coderhack.enums.status;
import com.buildout1.coderhack.exceptions.emailAlreadyExists;
import com.buildout1.coderhack.exceptions.invalidArg;
import com.buildout1.coderhack.exceptions.unexpectedBehaviour;
import com.buildout1.coderhack.exceptions.userNotFound;


@Service
public class userService {

    @Autowired
    private repo repository;

    public user saveuser(user user) {
        try {
            user existinguser = repository.findByEmail(user.getEmail());
            if (existinguser != null) {
                throw new emailAlreadyExists("Email already exist");
            }
            if (user.getEmail() != null && !user.getEmail().contains("@")) {
                throw new invalidArg("Please provide a valid email");
            }
            return repository.save(user);
        }  catch (invalidArg e) {
            throw new invalidArg("Please provide a important field");
        }catch (emailAlreadyExists e) {
            throw new emailAlreadyExists("Email already exist");
        }catch (unexpectedBehaviour e) {
            throw new unexpectedBehaviour("Unexpected error occurred while saving user. Please try again.");
        }
       
    }

    /*
     * Determines the badge for a user based on their score.
     *
     * @param score The score of the user.
     * 
     * @return The badge assigned to the user based on their score.
     */
    
    public badge setuserbadge(int score) {
        if (score >= 1 && score <= 30) {
            return badge.CODE_NINJA;
        } else if (score > 30 && score <= 60) {
            return badge.CODE_CHAMP;
        }else if(score > 60 && score <= 100) {
            return badge.CODE_MASTER;
        }
        return null;
    }

    
    public user updateuser(int score, Long id) {
        try {
            user existinguser = getuserByIdHelper(id);

            if (score < 0 || score > 100) {
                throw new invalidArg("score should be in between 0 to 100");
            }
            existinguser.setScore(score);

            badge badge= setuserbadge(score);
            Set<badge> badges= existinguser.getbadge();
            if(badges.isEmpty()) {
                badges= new HashSet<>();             
            }
            if(badge != null){
                badges.add(badge);
                existinguser.setbadge(badges);
            }
            return repository.save(existinguser);
            
        } catch (unexpectedBehaviour e) {
            throw new unexpectedBehaviour("Unexpected error occurred while updating user. Please try again.");
        }  catch (invalidArg e) {
            throw new invalidArg("score should be in between 0 to 100");
        }catch (userNotFound e) {
            throw new userNotFound("user not found");
        }
    }

    
    public List<user> getuser(String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "score");
        try {
            return repository.findAll(sort);
        } catch (Exception e) {
            throw new unexpectedBehaviour("Unexpected error occurred while fetching user. Please try again.");
        }
    }

    

    
    public void deleteuser(Long id) {
        try {
            getuserByIdHelper(id);
            repository.deleteById(id);
        }catch (userNotFound e) {
            throw new userNotFound("user not found");
        }catch (Exception e) {
            throw new unexpectedBehaviour("Unexpected error occurred while deleting user. Please try again.");
        }
    }

   

    
    public user getuserById(Long id) {
        try {
            return getuserByIdHelper(id);
        } catch (userNotFound e) {
            throw new userNotFound("user not found");
        } catch (Exception e) {
            throw new unexpectedBehaviour("Unexpected error occurred while fetching user. Please try again.");
        }
       
    }

    

    private user getuserByIdHelper(Long id) {
        Optional<user> user = repository.findById(id);
        if (!user.isPresent()) {
            throw new userNotFound("user not found");
        }
        return user.get();
    }

}