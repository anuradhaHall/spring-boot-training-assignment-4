package com.wipro.assignment;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.wipro.assignment.entities.User;
import com.wipro.assignment.repositories.UserRepository;
import com.wipro.assignment.services.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
public class UserServiceIntegrationTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user1;
    private List<User> users;

    @Before
    public void setUp(){
        user1 = new User("user 1");
        users = Stream.of(user1, new User("user 2"), new User("user 3")).collect(Collectors.toList());
        Mockito.when(userRepository.findByID(user1.getID())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.save(user1)).thenReturn(user1);
    }

    @Test
    public void whenFindAll_thenReturnAllUsersOK(){
        Mockito.when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<List<User>> expected = new ResponseEntity<List<User>>(users, HttpStatus.OK);
        ResponseEntity<List<User>> actual = userService.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenFindAllWithEmpty_thenReturnNotFound(){
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> expected = new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
        ResponseEntity<List<User>> actual = userService.findAll();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenValidId_thenUserIsFoundOK(){
        ResponseEntity<User> expected = new ResponseEntity<User>(user1, HttpStatus.OK);
        ResponseEntity<User> actual = userService.findById(user1.getID());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenInvalidId_thenUserNotFound(){
        ResponseEntity<User> expected = new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        ResponseEntity<User> actual = userService.findById(-1L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenAddUser_thenReturnUserOK(){
        ResponseEntity<User> expected = new ResponseEntity<User>(user1, HttpStatus.CREATED);
        ResponseEntity<User> actual = userService.addUser(user1);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenUpdateUser_thenReturnUserOK(){
        ResponseEntity<User> expected = new ResponseEntity<User>(user1, HttpStatus.OK);
        ResponseEntity<User> actual = userService.updateUser(user1);

        assertThat(actual).isEqualTo(expected);
    }

}
