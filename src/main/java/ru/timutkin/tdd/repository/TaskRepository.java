package ru.timutkin.tdd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.timutkin.tdd.entity.TaskEntity;
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>{
}
