package com.wipro.assignment;

import com.wipro.assignment.entities.User;
import com.wipro.assignment.repositories.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindById_thenReturnUser(){
        User expected = new User("User 1");
        entityManager.persistAndFlush(expected);

        User actual = userRepository.findByID(expected.getID()).orElse(null);
        assertThat(actual.getID()).isEqualTo(expected.getID());

    }

    @Test
    public void whenInvalidId_thenReturnNull(){
        Long id = -1L;
        User actual = userRepository.findByID(id).orElse(null);
        assertThat(actual).isNull();

    }

    @Test
    public void whenfindAll_thenReturnAllUsers(){
        List<User> expected = Stream.of(new User("user 1"), new User("user 2"), new User("user 3")).collect(Collectors.toList());
        expected.stream().forEach(u -> {entityManager.persist(u);});
        entityManager.flush();

        List<User> actual = userRepository.findAll();
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    public void whenNoUsers_thenReturnEmpty(){
        List<User> actual = userRepository.findAll();
        assertThat(actual).isEmpty();
    }

}
