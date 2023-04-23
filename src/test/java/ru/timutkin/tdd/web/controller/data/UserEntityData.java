package ru.timutkin.tdd.web.controller.data;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;
import ru.timutkin.tdd.entity.DepartmentEntity;
import ru.timutkin.tdd.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@UtilityClass
public class UserEntityData {

    private final List<UserEntity> userEntityList = new ArrayList<>();

    static {
        DepartmentEntity department = DepartmentEntity
                .builder()
                .name("IT")
                .build();


        Collections.addAll(userEntityList,
                UserEntity.builder()
                .email("xXwild.duckXx@yandex.ru")
                .firstName("Timofey")
                .middleName("Sergeevich")
                .department(department)
                .lastName("Utkin")
                .build(),
                UserEntity.builder()
                .email("goslingjava@coolman.com")
                .firstName("James")
                .middleName("Arthur")
                .lastName("Gosling")
                .department(department)
                .build()
        );
    }

    private final Random random = new Random();

    public static List<UserEntity> getListOfUserEntity(){
        return userEntityList;
    }

    public static UserEntity getAnyUserEntity(){
        return userEntityList.get(random.nextInt(0, userEntityList.size()-1));
    }

    public  UserEntity getFirst(){
        return userEntityList.get(0);
    }
}
