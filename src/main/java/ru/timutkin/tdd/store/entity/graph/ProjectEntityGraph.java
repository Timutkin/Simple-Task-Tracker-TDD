package ru.timutkin.tdd.store.entity.graph;

import jakarta.persistence.EntityGraph;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.timutkin.tdd.store.entity.ProjectEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ProjectEntityGraph {

    @Qualifier("projectEntityGraphWithTasksAndUserHead")
    EntityGraph<ProjectEntity> projectEntityGraphWithTasksAndUserHead;

    public ProjectEntityGraph( EntityGraph<ProjectEntity> projectEntityGraphWithTasksAndUserHead) {
        this.projectEntityGraphWithTasksAndUserHead = projectEntityGraphWithTasksAndUserHead;
    }

    public static Map<String, Object> getProperties(EntityGraph<ProjectEntity> entityGraph){
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);
        return properties;
    }
}
