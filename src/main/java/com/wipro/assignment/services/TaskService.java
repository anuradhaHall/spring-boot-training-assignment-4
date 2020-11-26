package com.wipro.assignment.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.wipro.assignment.entities.Task;
import com.wipro.assignment.entities.User;
import com.wipro.assignment.repositories.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public ResponseEntity<List<Task>> findAll(Long userId){
        User user = userService.findById(userId).getBody();
        if(!user.getTasks().isEmpty()){
            return new ResponseEntity<List<Task>>(user.getTasks(), HttpStatus.OK);
        }

        else{
            return new ResponseEntity<List<Task>>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Task>> findByCompleted(Long id, boolean completed){
        User user = userService.findById(id).getBody();

        //List<Task> tasks = taskRepository.findTasksByCompletedIn(completed, user.getTasks().stream().map(Task::getCompleted).collect(Collectors.toList()));
        
        List<Task> tasks = user.getTasks().stream().filter(t -> t.getCompleted() == completed).collect(Collectors.toList());
        if(!tasks.isEmpty()){
            return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
        }

        else{
            return new ResponseEntity<List<Task>>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Task> addTask(Long userId, Task task){
        User user = userService.findById(userId).getBody();
        user.getTasks().add(task);
        userService.updateUser(user);

        return new ResponseEntity<Task>(task, HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteTask(Long userId, Long id){
        taskRepository.deleteById(id);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    public ResponseEntity<Task> updateTaskCompletion(Long id, Boolean completed){
        Optional<Task> optional = taskRepository.findByID(id);
        
        if(optional.isPresent()){
            Task task = optional.get();
            task.setCompleted(completed);
            taskRepository.save(task);
            return new ResponseEntity<Task>(task, HttpStatus.OK);
        }

        else{
            return new ResponseEntity<Task>(HttpStatus.NOT_FOUND);
        }
    }
}
