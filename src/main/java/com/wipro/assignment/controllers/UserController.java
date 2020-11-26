package com.wipro.assignment.controllers;

import java.util.List;

import com.wipro.assignment.entities.User;
import com.wipro.assignment.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@Api(value = "Users", description = "Controller to find and add users")
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public ResponseEntity<List<User>> findAll(){
        return userService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findUser(@PathVariable(value = "id") Long id){
        return userService.findById(id);
    }

    @PostMapping(value="/")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }     
}
