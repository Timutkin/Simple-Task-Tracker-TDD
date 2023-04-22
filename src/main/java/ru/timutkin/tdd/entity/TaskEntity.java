package ru.timutkin.tdd.entity;



import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.timutkin.tdd.enumeration.Status;
import ru.timutkin.tdd.utils.DateFormatHM;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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
        this.dataTimeOfCreation = DateFormatHM.getDateTime();
        this.taskName = taskName;
        this.message = message;
        this.status = Status.OPEN;
    }
}
