package com.wipro.assignment.repositories;

import java.util.List;
import java.util.Optional;

import com.wipro.assignment.entities.Task;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TaskRepository extends CrudRepository<Task, Long>{
    Optional<Task> findByID(Long id);

    List<Task> findAll();

    //List<Task> findTasksByCompletedIn(List<Boolean> completed); Can't get this to work
}
