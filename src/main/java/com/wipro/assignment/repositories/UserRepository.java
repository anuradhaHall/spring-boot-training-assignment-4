package com.wipro.assignment.repositories;

import java.util.List;
import java.util.Optional;

import com.wipro.assignment.entities.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long>{
    Optional<User> findByID(Long id);
    List<User> findAll();
}
