package ru.timutkin.tdd.web.controller.data;

import lombok.experimental.UtilityClass;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.utils.DateFormatHM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class TaskDtoData {



    private static final List<TaskDto> validTaskDto = new ArrayList<>();

    static {
        Collections.addAll(validTaskDto,
                TaskDto.builder()
                        .id(1L)
                        .taskName("API - POST : /api/v1/users")
                        .createdAt(DateFormatHM.allTime)
                        .message("Message")
                        .status("OPEN")
                        .projectId(1L)
                        .build(),
                TaskDto.builder()
                        .id(2L)
                        .taskName("Task1")
                        .message("message")
                        .status("OPEN")
                        .createdAt(DateFormatHM.allTime)
                        .projectId(1L)
                        .build(),
                TaskDto.builder()
                        .id(3L)
                        .taskName("Task2")
                        .message("message")
                        .status("OPEN")
                        .projectId(1L)
                        .createdAt(DateFormatHM.allTime)
                        .build()
                );
    }

    public static List<TaskDto> getValidListTaskDto(){
        return validTaskDto;
    }

    public static TaskDto getFirstValidTaskDto(){
        return validTaskDto.get(0);
    }
}
