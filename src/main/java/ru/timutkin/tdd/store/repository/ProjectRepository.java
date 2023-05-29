package ru.timutkin.tdd.store.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.timutkin.tdd.store.entity.ProjectEntity;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    boolean existsByName(String name);

    @Override
    @EntityGraph("project-with-tasks-and-userHead")
    Optional<ProjectEntity> findById(Long id);
}