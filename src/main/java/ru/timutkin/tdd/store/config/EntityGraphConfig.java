package ru.timutkin.tdd.store.config;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.timutkin.tdd.store.entity.ProjectEntity;

@Configuration
@AllArgsConstructor
public class EntityGraphConfig {

    EntityManager entityManager;

    @Bean
    public EntityGraph<ProjectEntity> projectEntityGraphWithTasksAndUserHead() {
        EntityGraph<ProjectEntity> entityGraph = entityManager.createEntityGraph(ProjectEntity.class);
        entityGraph.addAttributeNodes("taskEntityList", "userHead");
        return entityGraph;
    }
}
