package com.wipro.assignment.services;

import java.util.List;
import java.util.Optional;

import com.wipro.assignment.entities.User;
import com.wipro.assignment.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<User>> findAll(){
        List<User> users = userRepository.findAll();
        if(!users.isEmpty()){
            return new ResponseEntity<List<User>>(users, HttpStatus.OK);
        } 
        
        else{
            return new  ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<User> findById(Long id){
        Optional<User> user = userRepository.findByID(id);

        if(user.isPresent()){
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        }

        else{
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }
 
    public ResponseEntity<User> addUser(User user){
        userRepository.save(user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    public ResponseEntity<User> updateUser(User user){
        userRepository.save(user);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}
