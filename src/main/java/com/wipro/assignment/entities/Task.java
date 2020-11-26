package com.wipro.assignment.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    private String task;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean completed;

public Task(){}

    public Task(String task, boolean completed) {
        super();
        this.task = task;
        this.completed = completed;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object object){
        if(object == null){
            return false;
        }

        if(object.getClass() != this.getClass()){
            return false;
        }

        Task other = (Task)object;
        if(other.getID() != this.getID()){
            return false;
        }

        return true;
    } 
}
