package ru.timutkin.tdd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.timutkin.tdd.entity.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
