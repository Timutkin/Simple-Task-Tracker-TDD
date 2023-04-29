package ru.timutkin.tdd.web.controller.data;

import lombok.experimental.UtilityClass;
import ru.timutkin.tdd.store.entity.DepartmentEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class DepartmentEntityData {

    private static final List<DepartmentEntity> validDepartmentEntityList = new ArrayList<>();

    static {
        Collections.addAll(validDepartmentEntityList,
                DepartmentEntity.builder().name("IT").build()
        );
    }

    public static List<DepartmentEntity> getValidDepartmentEntityList(){
        return validDepartmentEntityList;
    }

    public static DepartmentEntity getFirstDepartmentEntity(){
        return validDepartmentEntityList.get(0);
    }
}
