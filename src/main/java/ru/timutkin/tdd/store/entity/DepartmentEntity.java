package ru.timutkin.tdd.store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "department", schema = "public")
public class DepartmentEntity {

    @Id
    @SequenceGenerator(name = "DepartmentEntity_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DepartmentEntity_SEQ")
    Long id;
    @Column(unique = true)
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity departmentHead;

}