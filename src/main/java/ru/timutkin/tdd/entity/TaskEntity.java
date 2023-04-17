package ru.timutkin.tdd.entity;



import java.time.LocalDateTime;



public class TaskEntity {

    Long id;
    LocalDateTime dataTimeOfCreation;
    String taskName;
    String message;
    Boolean isDone;


    public TaskEntity(String taskName, String message) {
        this.dataTimeOfCreation = LocalDateTime.now();
        this.taskName = taskName;
        this.message = message;
        this.isDone = false;
    }
}
