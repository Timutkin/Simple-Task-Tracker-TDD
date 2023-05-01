package ru.timutkin.tdd.store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class UserEntity {

    @Id
    @SequenceGenerator(allocationSize = 20, name = "user_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    Long id;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;

    @Column(name = "middle_name")
    String middleName;

    @Column(unique = true)
    String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    DepartmentEntity department ;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<TaskEntity> taskEntityList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "userHead", fetch = FetchType.LAZY)
    List<ProjectEntity> projectEntityList = new ArrayList<>();

}
