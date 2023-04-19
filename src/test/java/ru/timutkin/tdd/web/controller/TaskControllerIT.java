package ru.timutkin.tdd.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.entity.TaskEntity;
import ru.timutkin.tdd.entity.UserEntity;

import ru.timutkin.tdd.enumeration.Status;
import ru.timutkin.tdd.repository.TaskRepository;
import ru.timutkin.tdd.repository.UserRepository;
import ru.timutkin.tdd.utils.DateFormatHM;
import ru.timutkin.tdd.web.constant.ApiConstant;


import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc()
class TaskControllerIT {

    @Autowired
    MockMvc mvc;

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;

    private static final UserEntity user = UserEntity.builder()
            .email("xXwild.duckXx@yandex.ru")
            .name("Timofey")
            .middleName("Sergeevich")
            .lastName("Utkin")
            .build();

    @AfterEach
    @Transactional
    void tearDown(){
        taskRepository.deleteById(1L);
        taskRepository.deleteById(2L);
        userRepository.deleteById(1L);
    }

    @Test
    void createTask_TaskIsValid_ReturnsValidResponse() throws Exception {
        userRepository.save(user);
        TaskDto task = TaskDto.builder()
                .id(1L)
                .dataTimeOfCreation(DateFormatHM.getDateTime())
                .taskName("task name")
                .message("message")
                .status(Status.OPEN)
                .userId(1L)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = mapper.writeValueAsString(task);
        mvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "taskName":"task name",
                            "message": "message",
                            "userId": 1
                        }
                        """)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json(json)
        );
    }

    @Test
    void createTask_TaskIsNonValidUserId_ReturnsValidResponse() throws Exception {
        TaskDto task = TaskDto.builder()
                .id(1L)
                .dataTimeOfCreation(DateFormatHM.getDateTime())
                .taskName("task name")
                .message("message")
                .status(Status.OPEN)
                .userId(1L)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = mapper.writeValueAsString(task);
        mvc.perform(post(ApiConstant.VERSION_API+"/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "taskName":"task name",
                            "message": "message",
                            "userId": 1
                        }
                        """)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void findAll_ReturnValidResponseEntity() throws Exception {
        UserEntity userEntity = user;
        userRepository.saveAndFlush(userEntity);
        String dateTime = "2023-04-19 23:52";
        taskRepository.saveAndFlush(
                TaskEntity.builder()
                        .dataTimeOfCreation(LocalDateTime.parse(dateTime,DateFormatHM.formatter))
                        .taskName("task1")
                        .message("message")
                        .status(Status.OPEN)
                        .user(user)
                        .build()
        );
        taskRepository.saveAndFlush(
                TaskEntity.builder()
                        .dataTimeOfCreation(LocalDateTime.parse(dateTime,DateFormatHM.formatter))
                        .taskName("task2")
                        .message("message")
                        .status(Status.OPEN)
                        .user(user)
                        .build()
        );
        mvc.perform(get(ApiConstant.VERSION_API+"/tasks"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                            [
                                {
                                    "id": 1,
                                    "dataTimeOfCreation":"2023-04-19T23:52:00",
                                    "taskName":"task1",
                                    "message": "message",
                                    "status": "OPEN",
                                    "userId": 1
                                },
                                {
                                    "id": 2,
                                    "dataTimeOfCreation":"2023-04-19T23:52:00",
                                    "taskName":"task2",
                                    "message": "message",
                                    "status": "OPEN",
                                    "userId": 1
                                }
                            ]
                             """
                        )
                );
    }
}