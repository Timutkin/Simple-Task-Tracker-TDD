package ru.timutkin.tdd.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user", schema = "public")
public class UserEntity {

    @Id
    @SequenceGenerator(allocationSize = 20, name = "user_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    Long id;

    String name;
    @Column(name = "last_name")
    String lastName;

    @Column(name = "middle_name")
    String middleName;

    String email;

    @ManyToOne(fetch = FetchType.LAZY)
    DepartmentEntity department;
}
