package ru.timutkin.tdd.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "project", schema = "public")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_entity_seq")
    @SequenceGenerator(name = "project_entity_seq")
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "project")
    private List<TaskEntity> entityList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userHead;

}