package ru.timutkin.tdd.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.timutkin.tdd.store.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
