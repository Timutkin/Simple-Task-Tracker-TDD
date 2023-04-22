package ru.timutkin.tdd.web.controller.data;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;
import ru.timutkin.tdd.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@UtilityClass
public class UserEntityData {

    private final List<UserEntity> userEntityList = new ArrayList<>();

    static {
        Collections.addAll(userEntityList,
                UserEntity.builder()
                .email("xXwild.duckXx@yandex.ru")
                .name("Timofey")
                .middleName("Sergeevich")
                .lastName("Utkin")
                .build(),
                UserEntity.builder()
                .email("goslingjava@coolman.com")
                .name("James")
                .middleName("Arthur")
                .lastName("Gosling ")
                .build()
        );
    }

    private final Random random = new Random();

    public List<UserEntity> getListOfUserEntity(){
        return userEntityList;
    }

    public UserEntity getAnyUserEntity(){
        return userEntityList.get(random.nextInt(0, userEntityList.size()-1));
    }

    public UserEntity getFirst(){
        return userEntityList.get(0);
    }
}
