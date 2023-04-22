package ru.timutkin.tdd.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "department")
public class DepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DepartmentEntity_SEQ")
    @SequenceGenerator(name = "DepartmentEntity_SEQ")
    Long id;

    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity departmentHead;

}