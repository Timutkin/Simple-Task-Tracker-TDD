package ru.timutkin.tdd.store.entity;



import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.timutkin.tdd.enumeration.Status;
import ru.timutkin.tdd.utils.DateFormatHM;

import java.time.LocalDateTime;


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

    @Column(name = "created_at")
    LocalDateTime createdAt;
    @Column(name = "task_name")
    String taskName;
    String message;
    @Enumerated(EnumType.STRING)
    Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    ProjectEntity project;


    public TaskEntity(String taskName, String message) {
        this.createdAt = DateFormatHM.getDateTime();
        this.taskName = taskName;
        this.message = message;
        this.status = Status.OPEN;
    }
}
