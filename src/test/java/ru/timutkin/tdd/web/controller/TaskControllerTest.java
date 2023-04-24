package ru.timutkin.tdd.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.exception.IncorrectFieldException;
import ru.timutkin.tdd.exception.IncorrectPathVariableException;
import ru.timutkin.tdd.service.TaskService;
import ru.timutkin.tdd.web.controller.data.TaskDtoData;
import ru.timutkin.tdd.web.request.CreationTaskRequest;

import java.util.List;

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
        TaskDto task = TaskDtoData.getFirstValidTaskDto();
        doReturn(task).when(this.taskService).save(request);
        //when
        var response = controller.createTask(request);
        //then
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType()),
                () -> assertEquals(task, response.getBody())
        );
    }

    @Test
    void createTask_TaskIsNonValid_ThrowsIncorrectFieldException() {
        CreationTaskRequest first = new CreationTaskRequest(null, "message", 1L);
        CreationTaskRequest second = new CreationTaskRequest("   ", "message", 1L);
        CreationTaskRequest third = new CreationTaskRequest("Task name", null, 1L);
        CreationTaskRequest fourth = new CreationTaskRequest("Task name", "  ", 1L);
        CreationTaskRequest fifth = new CreationTaskRequest("Task name", "  ", null);
        CreationTaskRequest sixth = new CreationTaskRequest("Task name", "message", -100L);
        assertAll(
                () -> assertThrows(IncorrectFieldException.class, () -> controller.createTask(first)),
                () -> assertThrows(IncorrectFieldException.class, () -> controller.createTask(second)),
                () -> assertThrows(IncorrectFieldException.class, () -> controller.createTask(third)),
                () -> assertThrows(IncorrectFieldException.class, () -> controller.createTask(fourth)),
                () -> assertThrows(IncorrectFieldException.class, () -> controller.createTask(fifth)),
                () -> assertThrows(IncorrectFieldException.class, () -> controller.createTask(sixth))
        );
    }

    @Test
    void findAll_ReturnValidResponseEntity() {
        List<TaskDto> taskDtoList = TaskDtoData.getValidListTaskDto();
        doReturn(taskDtoList).when(this.taskService).findAll();
        var response = controller.findAll();
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType()),
                () -> assertEquals(taskDtoList, response.getBody())
        );
    }


    @Test
    void updateTask_TaskIsNonValid_ThrowsIncorrectFieldException() {
        List<TaskDto> nonValidTaskDto = TaskDtoData.getNonValidListTaskDto();
        for (TaskDto dto : nonValidTaskDto) {
            assertThrows(IncorrectFieldException.class, () -> controller.updateTask(dto));
        }
    }

    @Test
    void updateTask_TaskIsValid_ReturnsValidResponseEntity() {
        TaskDto taskDtoUpdateRequest = TaskDto.builder()
                .id(1L)
                .status("CLOSED")
                .userId(2L)
                .build();
        TaskDto updatedTaskDto = TaskDtoData.getFirstValidTaskDto();
        updatedTaskDto.setStatus("CLOSED");
        updatedTaskDto.setUserId(2L);
        doReturn(updatedTaskDto).when(this.taskService).update(taskDtoUpdateRequest);
        var response = controller.updateTask(taskDtoUpdateRequest);
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType()),
                () -> assertEquals(updatedTaskDto, response.getBody())
        );
    }

    @Test
    void deleteById_TaskIdIsValid_ReturnsValidResponseEntity() {
        Long taskId = 1L;
        var response = controller.deleteById(taskId);
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType()),
                () -> assertEquals(taskId, response.getBody())
        );
        Mockito.verify(this.taskService, Mockito.times(1)).deleteById(taskId);
    }


    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -3L})
    @NullSource
    void deleteById_TaskIdIsNonValid_ThrowsIncorrectPathVariableException(Long taskId) {
        assertThrows(IncorrectPathVariableException.class, ()->controller.deleteById(taskId));
    }
}