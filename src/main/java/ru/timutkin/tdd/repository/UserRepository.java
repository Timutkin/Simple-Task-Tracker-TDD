package ru.timutkin.tdd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.timutkin.tdd.entity.UserEntity;



public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
