package ru.timutkin.tdd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.timutkin.tdd.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
