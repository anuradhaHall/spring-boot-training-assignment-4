package com.wipro.assignment;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.assignment.controllers.UserController;
import com.wipro.assignment.entities.User;
import com.wipro.assignment.services.UserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerIntegrationTests {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void whenFindAll_thenReturnAll() throws Exception{
        List<User> users = Stream.of(new User("user 1"), new User("user 2"), new User("user 3")).collect(Collectors.toList());

        when(userService.findAll()).thenReturn(new ResponseEntity<List<User>>(users,HttpStatus.OK));

        mvc.perform(get("/users/").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].id").value(users.get(0).getID()))
        .andExpect(jsonPath("$[1].id").value(users.get(1).getID()))
        .andExpect(jsonPath("$[2].id").value(users.get(2).getID()));
        
    }

    @Test
    public void whenFindValidUser_thenReturnUser() throws Exception{
        User user = new User("user 1");

        when(userService.findById(1L)).thenReturn(new ResponseEntity<User>(user, HttpStatus.OK));

        mvc.perform(get("/users/{id}", 1).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(user.getID()))
        .andExpect(jsonPath("$.name").value(user.getName()));
    }

    @Test
    public void whenFindInvalidUser_thenReturnNotFound() throws Exception{

        when(userService.findById(-1L)).thenReturn(new ResponseEntity<User>(HttpStatus.NOT_FOUND));

        mvc.perform(get("/users/{id}", -1).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
    }

    @Test
    public void whenAddUser_ReturnOk() throws Exception{
        User user = new User("user 1");
        user.setID(1L);

        when(userService.addUser(user)).thenReturn(new ResponseEntity<User>(user, HttpStatus.CREATED));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user); 

        mvc.perform(post("/users/").contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(json)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    }
}
