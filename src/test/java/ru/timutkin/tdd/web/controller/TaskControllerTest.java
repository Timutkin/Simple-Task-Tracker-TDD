package ru.timutkin.tdd.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.enumeration.Status;
import ru.timutkin.tdd.service.TaskService;
import ru.timutkin.tdd.web.request.CreationTaskRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    TaskService taskService;

    @InjectMocks
    TaskController controller;


    @Test
    void createTask_TaskIsValid_ReturnsValidResponse() {

        // given
        CreationTaskRequest request = CreationTaskRequest
                .builder()
                .taskName("API - POST : /api/v1/users")
                .message("Message")
                .userId(1L)
                .build();
        TaskDto task = TaskDto
                .builder()
                .id(1L)
                .taskName("API - POST : /api/v1/users")
                .dataTimeOfCreation(LocalDateTime.now())
                .message("Message")
                .userId(1L)
                .status(Status.OPEN)
                .build();
        doReturn(task)
                .when(this.taskService).save(request);
        //when
        var response =  controller.createTask(request);
        //then
        assertAll(
                ()-> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType()),
                () -> assertEquals(task, response.getBody())
        );


    }
}