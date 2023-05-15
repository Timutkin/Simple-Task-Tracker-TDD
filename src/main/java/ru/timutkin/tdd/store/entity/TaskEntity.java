package ru.timutkin.tdd.store.entity;



import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.timutkin.tdd.enumeration.Status;
import ru.timutkin.tdd.utils.DateFormatHM;

import java.time.LocalDateTime;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks", schema = "public", uniqueConstraints = {@UniqueConstraint(columnNames = {"task_name", "message"})})
public class TaskEntity {

    @Id
    @SequenceGenerator(allocationSize = 20, name = "task_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    Long id;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt = DateFormatHM.getDateTime();
    @Column(name = "task_name", nullable = false)
    String taskName;
    @Column(nullable = false)
    String message;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "project_id")
    ProjectEntity project;

    public TaskEntity(String taskName, String message) {
        this.taskName = taskName;
        this.message = message;
        this.status = Status.OPEN;
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
               "id=" + id +
               ", createdAt=" + createdAt +
               ", taskName='" + taskName + '\'' +
               ", message='" + message + '\'' +
               ", status=" + status +
               '}';
    }
}
