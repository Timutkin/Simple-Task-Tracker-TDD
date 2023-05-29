package ru.timutkin.tdd.web.controller.data;

import lombok.experimental.UtilityClass;
import ru.timutkin.tdd.store.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class UserEntityData {

    private static final List<UserEntity> userEntityList = new ArrayList<>();

    static {
        Collections.addAll(userEntityList,
                UserEntity.builder()
                .email("xXwild.duckXx@yandex.ru")
                .firstName("Timofey")
                .middleName("Sergeevich")
                .lastName("Utkin")
                .build(),
                UserEntity.builder()
                .email("goslingjava@coolman.com")
                .firstName("James")
                .middleName("Arthur")
                .lastName("Gosling")
                .build()
        );
    }

    private static final Random random = new Random();

    public static List<UserEntity> getListOfUserEntity(){
        return userEntityList;
    }

    public static UserEntity getAnyUserEntity(){
        return userEntityList.get(random.nextInt(0, userEntityList.size()-1));
    }

    public static   UserEntity getFirstUserEntity(){
        return userEntityList.get(0);
    }

    public static UserEntity getSecondUserEntity(){
        return userEntityList.get(1);
    }
}
