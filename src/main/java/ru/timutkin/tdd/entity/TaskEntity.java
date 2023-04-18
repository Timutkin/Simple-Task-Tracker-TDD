package ru.timutkin.tdd.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.timutkin.tdd.enumeration.Status;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tasks", schema = "public")
public class TaskEntity {

    @Id
    @SequenceGenerator(allocationSize = 20, name = "task_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    Long id;

    @Column(name = "data_time_of_creation")
    LocalDateTime dataTimeOfCreation;
    @Column(name = "task_name")
    String taskName;
    String message;
    @Enumerated(EnumType.STRING)
    Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity user;


    public TaskEntity(String taskName, String message) {
        this.dataTimeOfCreation = LocalDateTime.now();
        this.taskName = taskName;
        this.message = message;
        this.status = Status.OPEN;
    }
}
