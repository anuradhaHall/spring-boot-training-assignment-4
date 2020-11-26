package com.wipro.assignment.controllers;

import java.util.List;

import com.wipro.assignment.entities.Task;
import com.wipro.assignment.services.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(value = "tasks", description = "Controller to add, find, update and delete tasks")
@RequestMapping("/users")
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @GetMapping(value = "/{id}/tasks")
    public ResponseEntity<List<Task>> getTasks(@PathVariable(value = "id") Long id){
        return taskService.findAll(id);
    }

    @GetMapping("/{id}/tasks/complete")
    @ResponseBody
    public ResponseEntity<List<Task>> getComplete(@PathVariable(value = "id") Long id){
        return taskService.findByCompleted(id, true);
    }
    
    @GetMapping("/{id}/tasks/incomplete")
    @ResponseBody
    public ResponseEntity<List<Task>> getIncomplete(@PathVariable(value = "id") Long id){
        return taskService.findByCompleted(id, false);
    }

    
    @PostMapping("/{id}/tasks")
    public ResponseEntity<Task> addTask(@PathVariable(value = "id") Long id, @RequestBody Task task){
        return taskService.addTask(id, task);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<String> deleteTask(@PathVariable(value = "id") Long userId, @RequestParam(value = "id") Long id){
        return taskService.deleteTask(userId, id);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<Task> updateTask(@RequestParam(value = "id") Long id, @RequestParam(value = "completed") Boolean completed){
        return taskService.updateTaskCompletion(id, completed);
    }
}
