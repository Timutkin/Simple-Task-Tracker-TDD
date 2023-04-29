package ru.timutkin.tdd.store.repository.specifiction;

import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.timutkin.tdd.store.entity.TaskEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class TaskSpecification {

    public static Specification<TaskEntity> filterTasks(Optional<LocalDateTime> after, Optional<LocalDateTime> before,
                                                        Optional<String> taskName, Optional<String> message,
                                                        Optional<String> status, Optional<Long> userId, Optional<Long> projectId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            taskName.ifPresent(s -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("taskName")), "%" + s.toLowerCase() + "%")
            )));
            message.ifPresent(s -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("message")), "%" + s.toLowerCase() + "%")
            )));
            status.ifPresent(s -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), "%" + s.toLowerCase() + "%")
            )));
            userId.ifPresent(aLong -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("user"), aLong)
            )));
            projectId.ifPresent(aLong -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("projectId"), aLong)
            )));
            if (after.isPresent() && before.isPresent()){
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), after.get())));
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), before.get())));
            }
            after.ifPresent(localDateTime -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), localDateTime))));
            before.ifPresent(localDateTime -> predicates.add(criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), localDateTime))));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

    }
}