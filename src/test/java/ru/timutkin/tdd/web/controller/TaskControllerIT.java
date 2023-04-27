package ru.timutkin.tdd.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.tdd.dto.CreationTaskRequest;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.entity.TaskEntity;
import ru.timutkin.tdd.entity.UserEntity;
import ru.timutkin.tdd.mapper.TaskMapper;
import ru.timutkin.tdd.repository.TaskRepository;
import ru.timutkin.tdd.repository.UserRepository;
import ru.timutkin.tdd.utils.DateFormatHM;
import ru.timutkin.tdd.utils.JsonConverter;
import ru.timutkin.tdd.web.constant.ApiConstant;
import ru.timutkin.tdd.web.controller.data.TaskDtoData;
import ru.timutkin.tdd.web.controller.data.TaskEntityData;
import ru.timutkin.tdd.web.controller.data.UserEntityData;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TaskControllerIT {

    @Autowired
    MockMvc mvc;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JsonConverter jsonConverter;
    @Autowired
    TaskMapper taskMapper;

    @Test
    void createTask_TaskIsValid_ReturnsValidResponse() throws Exception {
        UserEntity user = UserEntityData.getFirstUserEntity();
        userRepository.save(user);
        TaskDto task = TaskDtoData.getFirstValidTaskDto();
        task.setUserId(user.getId());
        task.setDataTimeOfCreation(DateFormatHM.getDateTime());
        String jsonTask = jsonConverter.convert(task);
        CreationTaskRequest request = new CreationTaskRequest("API - POST : /api/v1/users", "Message", 1L);
        String jsonTaskRequest = jsonConverter.convert(request);
        mvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTaskRequest)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json(jsonTask)
        );
    }

    @Test
    void createTask_TaskIsNonValidUserId_ReturnsBadRequest() throws Exception {
        CreationTaskRequest first = new CreationTaskRequest("   ", "message", 1L);
        CreationTaskRequest second = new CreationTaskRequest("task name", " ", 1L);
        CreationTaskRequest third = new CreationTaskRequest("task name", "message", 1L);
        mvc.perform(post(ApiConstant.VERSION_API + "/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convert(first))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
        mvc.perform(post(ApiConstant.VERSION_API + "/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convert(second))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
        mvc.perform(post(ApiConstant.VERSION_API + "/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convert(third))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    void findAll_WithoutRequestParam_ReturnValidResponseEntity() throws Exception {
        UserEntity userEntity = UserEntityData.getFirstUserEntity();
        userRepository.save(userEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        TaskEntity second = TaskEntityData.getValidTaskEntityList().get(1);
        first.setUser(userEntity);
        second.setUser(userEntity);
        taskRepository.save(first);
        taskRepository.save(second);
        List<TaskDto> taskEntityList = List.of(taskMapper.taskEntityToTaskDto(first), taskMapper.taskEntityToTaskDto(second));
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonConverter.convert(taskEntityList)
                        )
                );
    }

    @Test
    void findAll_RequestParamAfterBefore_ReturnsValidResponseEntity() throws Exception {
        UserEntity userEntity = UserEntityData.getFirstUserEntity();
        userRepository.save(userEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        TaskEntity second = TaskEntityData.getValidTaskEntityList().get(1);
        first.setUser(userEntity);
        second.setUser(userEntity);
        taskRepository.save(first);
        taskRepository.save(second);
        List<TaskDto> taskEntityList = List.of(taskMapper.taskEntityToTaskDto(first), taskMapper.taskEntityToTaskDto(second));
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("after", "2023-03-02T00:00"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskEntityList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("after", "2023-03-05T00:00"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("before", "2023-03-04T00:00"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskEntityList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("before", "2023-03-02T00:00"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks")
                        .param("before", "2023-03-04T00:00")
                        .param("after", "2023-03-02T00:00")
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskEntityList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void findAll_RequestParamTaskNameMessage_ReturnsValidResponseEntity() throws Exception {
        UserEntity userEntity = UserEntityData.getFirstUserEntity();
        userRepository.save(userEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        TaskEntity second = TaskEntityData.getValidTaskEntityList().get(1);
        first.setUser(userEntity);
        second.setUser(userEntity);
        taskRepository.save(first);
        taskRepository.save(second);
        List<TaskDto> taskDtoList = List.of(taskMapper.taskEntityToTaskDto(first), taskMapper.taskEntityToTaskDto(second));
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("taskName", "sk"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskDtoList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("taskName", "www"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("message", "ssa"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskDtoList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("message", "www"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks")
                        .param("taskName", "2")
                        .param("message", "ssa")
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(List.of(taskMapper.taskEntityToTaskDto(second)))),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void findAll_RequestParamStatusUserId_ReturnsValidResponseEntity() throws Exception {
        UserEntity userEntity = UserEntityData.getFirstUserEntity();
        userRepository.save(userEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        TaskEntity second = TaskEntityData.getValidTaskEntityList().get(1);
        first.setUser(userEntity);
        second.setUser(userEntity);
        taskRepository.save(first);
        taskRepository.save(second);
        List<TaskDto> taskDtoList = List.of(taskMapper.taskEntityToTaskDto(first), taskMapper.taskEntityToTaskDto(second));
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("status", "OPEN"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskDtoList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("status", "CLOSED"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks")
                        .param("status", "OPEN")
                        .param("userId", String.valueOf(1L))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskDtoList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks")
                        .param("status", "OPEN")
                        .param("userId", String.valueOf(2L))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void findAll_RequestParamIsNonValid_ReturnValidResponseEntity() throws Exception {
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("after", LocalDateTime.now().toString()))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("before", LocalDateTime.now().toString()))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("taskName", ""))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("message", ""))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("status", "open"))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("userId", String.valueOf(-1L)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void updateTask_TaskIsValid_ReturnsValidResponseEntity() throws Exception {
        UserEntity firstUserEntity = UserEntityData.getFirstUserEntity();
        UserEntity secondUserEntity = UserEntityData.getListOfUserEntity().get(1);
        userRepository.save(firstUserEntity);
        userRepository.save(secondUserEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        first.setUser(firstUserEntity);
        taskRepository.save(first);
        TaskDto updatedTaskDto = new TaskDto(1L, DateFormatHM.allTime, "new task", "new message", "CLOSED", 2L);
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(updatedTaskDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonConverter.convert(updatedTaskDto))
                );
    }

    @Test
    void updateTask_TaskIsNonValid_ReturnsValidResponseEntity() throws Exception {
        TaskDto first = TaskDto.builder().id(0L).build();
        TaskDto second = TaskDto.builder().id(1L).dataTimeOfCreation(LocalDateTime.now()).build();
        TaskDto third = TaskDto.builder().id(1L).taskName("  ").build();
        TaskDto fourth = TaskDto.builder().id(1L).message(" ").build();
        TaskDto fifth = TaskDto.builder().id(1L).status("open").build();
        TaskDto sixth = TaskDto.builder().id(1L).userId(0L).build();
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(first))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(second))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(third))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(fourth))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(fifth))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(sixth))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void deleteById_TaskIdIsValid_ReturnsValidResponseEntity() throws Exception {
        UserEntity user = UserEntityData.getFirstUserEntity();
        userRepository.save(user);
        TaskEntity task = TaskEntityData.getFirstValidTaskEntity();
        task.setUser(user);
        taskRepository.save(task);
        Long taskId = task.getId();
        mvc.perform(delete(ApiConstant.VERSION_API + "/tasks/{taskID}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(String.valueOf(taskId))
                );
    }

    @Test
    void deleteById_TaskIdIsNonValid_ReturnsBadRequest() throws Exception {
        Long firstTaskId = 1L;
        Long secondTaskId = 0L;
        mvc.perform(delete(ApiConstant.VERSION_API + "/tasks/{taskID}", firstTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(delete(ApiConstant.VERSION_API + "/tasks/{taskID}", secondTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void findById_TaskIdIsValid_ReturnsValidResponseEntity() throws Exception {
        userRepository.save(UserEntityData.getFirstUserEntity());
        TaskEntity task = TaskEntityData.getFirstValidTaskEntity();
        task.setUser(UserEntityData.getFirstUserEntity());
        taskRepository.save(task);
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks/{taskId}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonConverter.convert(taskMapper.taskEntityToTaskDto(task)))
                );
    }

    @Test
    void findById_TaskIdIsNonValid_ReturnsValidResponseEntity() throws Exception {
        Long taskId = 1L;
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }
}