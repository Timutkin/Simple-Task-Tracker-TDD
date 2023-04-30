package ru.timutkin.tdd.store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.timutkin.tdd.utils.DateFormatHM;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project", schema = "public")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_entity_seq")
    @SequenceGenerator(name = "project_entity_seq")
    private Long id;

    @Column(unique = true)
    private String name;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = DateFormatHM.getDateTime();

    @Builder.Default
    @OneToMany(mappedBy = "project")
    private List<TaskEntity> entityList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userHead;

}