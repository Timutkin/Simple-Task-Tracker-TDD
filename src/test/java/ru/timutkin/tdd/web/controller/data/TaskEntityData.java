package ru.timutkin.tdd.web.controller.data;

import lombok.experimental.UtilityClass;
import ru.timutkin.tdd.store.entity.TaskEntity;
import ru.timutkin.tdd.enumeration.Status;
import ru.timutkin.tdd.utils.DateFormatHM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class TaskEntityData {

    private static final List<TaskEntity> validTaskEntityList = new ArrayList<>();

    static {
        Collections.addAll(validTaskEntityList,
                TaskEntity.builder()
                        .createdAt(DateFormatHM.allTime)
                        .taskName("task1")
                        .message("message")
                        .status(Status.OPEN)
                        .build()
                ,
                TaskEntity.builder()
                        .createdAt(DateFormatHM.allTime)
                        .taskName("task2")
                        .message("message")
                        .status(Status.OPEN)
                        .build());

    }


    public List<TaskEntity> getValidTaskEntityList(){
        return validTaskEntityList;
    }

    public TaskEntity   getFirstValidTaskEntity(){
        return validTaskEntityList.get(0);
    }
}
