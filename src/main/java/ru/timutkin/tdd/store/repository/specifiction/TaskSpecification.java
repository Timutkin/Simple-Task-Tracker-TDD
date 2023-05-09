package ru.timutkin.tdd.store.repository.specifiction;

import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.timutkin.tdd.dto.param.FilterTaskParams;
import ru.timutkin.tdd.store.entity.TaskEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class TaskSpecification {

    public static Specification<TaskEntity> filterTasks(FilterTaskParams filterTaskParams) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            filterTaskParams.taskName().ifPresent(s -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("taskName")), "%" + s.toLowerCase() + "%")
            )));
            filterTaskParams.message().ifPresent(s -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("message")), "%" + s.toLowerCase() + "%")
            )));
            filterTaskParams.status().ifPresent(s -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), "%" + s.toLowerCase() + "%")
            )));
            filterTaskParams.userId().ifPresent(aLong -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("user"), aLong)
            )));
            filterTaskParams.projectId().ifPresent(aLong -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("project"), aLong)
            )));
            if (filterTaskParams.after().isPresent() && filterTaskParams.before().isPresent()){
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filterTaskParams.after().get())));
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), filterTaskParams.before().get())));
            }
            filterTaskParams.after().ifPresent(localDateTime -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), localDateTime))));
            filterTaskParams.before().ifPresent(localDateTime -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), localDateTime))));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

    }

}
