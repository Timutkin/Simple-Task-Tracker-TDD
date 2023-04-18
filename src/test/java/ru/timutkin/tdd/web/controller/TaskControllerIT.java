package ru.timutkin.tdd.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.entity.UserEntity;

import ru.timutkin.tdd.enumeration.Status;
import ru.timutkin.tdd.repository.TaskRepository;
import ru.timutkin.tdd.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @AfterEach
    void tearDown(){
        userRepository.deleteById(1L);
    }

    @Test
    void createTask_TaskIsValid_ReturnsValidResponse() throws Exception {
        userRepository.save(UserEntity.builder()
                        .email("xXwild.duckXx@yandex.ru")
                        .name("Timofey")
                        .middleName("Sergeevich")
                        .lastName("Utkin")
                .build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(formatter);
        System.out.println(formattedDateTime);
        TaskDto task = TaskDto.builder()
                .id(1L)
                .dataTimeOfCreation(LocalDateTime.parse(formattedDateTime, formatter))
                .taskName("task name")
                .message("message")
                .status(Status.OPEN)
                .userId(1L)
                .build();
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String json = jsonMapper.writeValueAsString(task);
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
}