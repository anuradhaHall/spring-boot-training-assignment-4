package com.wipro.assignment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.wipro.assignment.entities.Task;
import com.wipro.assignment.entities.User;
import com.wipro.assignment.repositories.TaskRepository;
import com.wipro.assignment.services.TaskService;
import com.wipro.assignment.services.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TaskServiceIntegrationTests {
    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    Task task1;
    List<Task> tasks;

    @Mock
    User user;

    @Before
    public void setUp(){
        task1 = new Task("incomplete task", false);
        tasks = Stream.of(task1, new Task("complete task", true), new Task("another incomplete task", false)).collect(Collectors.toList());
        
        user = new User("user");
        user.setTasks(tasks);

        when(userService.findById(1L)).thenReturn(new ResponseEntity<User>(user, HttpStatus.OK));
        when(taskRepository.findByID(task1.getID())).thenReturn(Optional.of(task1));    
        when(taskRepository.save(task1)).thenReturn(task1);
    }

    @Test
    public void whenFindAll_thenReturnAllTasksOK(){

        ResponseEntity<List<Task>> expected = new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
        ResponseEntity<List<Task>> actual = taskService.findAll(1L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenFindAllWithEmpty_thenReturnNotFound(){
        user.getTasks().clear();
        ResponseEntity<List<Task>> expected = new ResponseEntity<List<Task>>(HttpStatus.NOT_FOUND);
        ResponseEntity<List<Task>> actual = taskService.findAll(1L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenFindByCompleted_thenReturnAllCompletedOK(){
        ResponseEntity<List<Task>> expected = new ResponseEntity<List<Task>>(user.getTasks().stream().filter(t -> t.getCompleted() == true).collect(Collectors.toList()), HttpStatus.OK);
        ResponseEntity<List<Task>> actual = taskService.findByCompleted(1L, true);
        
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenFindByIncomplete_thenReturnAllIncompleteOK(){
        ResponseEntity<List<Task>> expected = new ResponseEntity<List<Task>>(user.getTasks().stream().filter(t -> t.getCompleted() == false).collect(Collectors.toList()), HttpStatus.OK);
        ResponseEntity<List<Task>> actual = taskService.findByCompleted(1L, false);
        
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenFindByCompletedEmpty_thenReturnNotFound(){
        user.getTasks().clear();

        ResponseEntity<List<Task>> expected = new ResponseEntity<List<Task>>(HttpStatus.NOT_FOUND);
        ResponseEntity<List<Task>> actual = taskService.findByCompleted(1L, true);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenFindByIncompleteEmpty_thenReturnNotFound(){
        user.getTasks().clear();

        ResponseEntity<List<Task>> expected = new ResponseEntity<List<Task>>(HttpStatus.NOT_FOUND);
        ResponseEntity<List<Task>> actual = taskService.findByCompleted(1L, false);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenAddTask_thenReturnTaskOK(){
        ResponseEntity<Task> expected = new ResponseEntity<Task>(task1, HttpStatus.CREATED);
        ResponseEntity<Task> actual = taskService.addTask(1L, task1);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenUpdateValidTask_thenReturnTaskOK(){
        ResponseEntity<Task> expected = new ResponseEntity<Task>(task1, HttpStatus.OK);
        ResponseEntity<Task> actual = taskService.updateTaskCompletion(task1.getID(), false);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenUpdateInvalidTask_thenReturnTaskNotFound(){
        ResponseEntity<Task> expected = new ResponseEntity<Task>(HttpStatus.NOT_FOUND);
        ResponseEntity<Task> actual = taskService.updateTaskCompletion(-1L, false);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenDeleteTask_thenReturnOK(){
        ResponseEntity<String> expected = new ResponseEntity<String>(HttpStatus.OK);
        ResponseEntity<String> actual = taskService.deleteTask(user.getID(), task1.getID()); 
        
        assertThat(actual).isEqualTo(expected);
    }
}
