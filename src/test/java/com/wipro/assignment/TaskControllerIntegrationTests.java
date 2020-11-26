package com.wipro.assignment;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.assignment.controllers.TaskController;
import com.wipro.assignment.entities.Task;
import com.wipro.assignment.services.TaskService;

import org.junit.Before;
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
@WebMvcTest(TaskController.class)
public class TaskControllerIntegrationTests {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService taskService;

    private Task task1;
    private List<Task> tasks;
    
    @Before
    public void setUp(){
        task1 = new Task("incomplete 1", false);
        task1.setID(1L);
        tasks = Stream.of(task1, new Task("incomplete 2", false), new Task("complete 1", true)).collect(Collectors.toList());
    }

    @Test
    public void whenGetTasks_thenReturnAllOK() throws Exception{
        when(taskService.findAll(1L)).thenReturn(new ResponseEntity<List<Task>>(tasks, HttpStatus.OK));

        mvc.perform(get("/users/1/tasks").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].id").value(tasks.get(0).getID()))
        .andExpect(jsonPath("$[1].id").value(tasks.get(1).getID()))
        .andExpect(jsonPath("$[2].id").value(tasks.get(2).getID()));
    }

    @Test
    public void whenGetTasksInvalid_thenReturnNotFound() throws Exception{
        when(taskService.findAll(2L)).thenReturn(new ResponseEntity<List<Task>>(HttpStatus.NOT_FOUND));

        mvc.perform(get("/users/2/tasks").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetCompleteTasks_thenReturnAllOK() throws Exception{
        when(taskService.findByCompleted(1L, true)).thenReturn(new ResponseEntity<List<Task>>(tasks.stream().filter(t -> t.getCompleted() == true).collect(Collectors.toList()), HttpStatus.OK));

        mvc.perform(get("/users/1/tasks/complete").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].completed").value(true));
    }

    @Test
    public void whenGetCompleteTasksEmpty_thenReturnNotFound() throws Exception{
        when(taskService.findByCompleted(1L, true)).thenReturn(new ResponseEntity<List<Task>>(HttpStatus.NOT_FOUND));

        mvc.perform(get("/users/1/tasks/complete").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetInCompleteTasks_thenReturnAllOK() throws Exception{
        when(taskService.findByCompleted(1L, false)).thenReturn(new ResponseEntity<List<Task>>(tasks.stream().filter(t -> t.getCompleted() == false).collect(Collectors.toList()), HttpStatus.OK));

        mvc.perform(get("/users/1/tasks/incomplete").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    public void whenGetInompleteTasksEmpty_thenReturnNotFound() throws Exception{
        when(taskService.findByCompleted(1L, false)).thenReturn(new ResponseEntity<List<Task>>(HttpStatus.NOT_FOUND));

        mvc.perform(get("/users/1/tasks/incomplete").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    public void whenAddTask_thenReturnTaskCreated() throws Exception{
        when(taskService.addTask(1L, task1)).thenReturn(new ResponseEntity<Task>(task1, HttpStatus.CREATED));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(task1);

        mvc.perform(post("/users/1/tasks").contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated());
    }

    @Test
    public void whenDeleteTask_thenReturnOK() throws Exception{
        when(taskService.deleteTask(1L, task1.getID())).thenReturn(new ResponseEntity<String>(HttpStatus.OK));

        mvc.perform(post("/users/1/delete?id={id}", task1.getID()).contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateTask_thenReturnOK() throws Exception{
        task1.setCompleted(true);
        when(taskService.updateTaskCompletion(task1.getID(), true)).thenReturn(new ResponseEntity<Task>(task1, HttpStatus.OK));

        mvc.perform(post("/users/1/update?id={id}&completed=true", task1.getID()).contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    public void whenUpdateTaskInvalid_thenReturnNotFound() throws Exception{
        when(taskService.updateTaskCompletion(-1L, true)).thenReturn(new ResponseEntity<Task>(HttpStatus.NOT_FOUND));

        mvc.perform(post("/users/1/update?id={id}&completed=true", -1L).contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
    }
}
