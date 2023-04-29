package ru.timutkin.tdd.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.timutkin.tdd.store.entity.TaskEntity;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {

    @Query("SELECT t FROM TaskEntity t WHERE t.user.id = :userId")
    List<TaskEntity> findTaskEntityByUserId(@Param("userId") Long id);
}
