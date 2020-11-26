package com.wipro.assignment;

import com.wipro.assignment.entities.Task;
import com.wipro.assignment.repositories.TaskRepository;

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
public class TaskRepositoryIntegrationTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void whenFindById_thenReturnTask(){
        Task expected = new Task("Task 1", false);
        entityManager.persistAndFlush(expected);

        Task actual = taskRepository.findByID(expected.getID()).orElse(null);
        assertThat(actual.getID()).isEqualTo(expected.getID());
    }

    @Test
    public void whenInvalidId_thenReturnNull(){
        Long id = -1L;
        
        Task actual = taskRepository.findByID(id).orElse(null);
        assertThat(actual).isNull();
    }

    @Test
    public void whenfindAll_thenReturnAllTasks(){
        List<Task> expected = Stream.of(new Task("task 1", false), new Task("task 2", false), new Task("task 3", false)).collect(Collectors.toList());
        expected.stream().forEach(u -> {entityManager.persist(u);});
        entityManager.flush();

        List<Task> actual = taskRepository.findAll();
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    public void whenNoUsers_thenReturnEmpty(){
        List<Task> actual = taskRepository.findAll();
        assertThat(actual).isEmpty();
    } 
}
