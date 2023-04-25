package ru.timutkin.tdd.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.entity.TaskEntity;
import ru.timutkin.tdd.entity.UserEntity;

import ru.timutkin.tdd.repository.DepartmentRepository;
import ru.timutkin.tdd.repository.TaskRepository;
import ru.timutkin.tdd.repository.UserRepository;
import ru.timutkin.tdd.utils.DateFormatHM;
import ru.timutkin.tdd.web.constant.ApiConstant;
import ru.timutkin.tdd.web.controller.data.TaskDtoData;
import ru.timutkin.tdd.web.controller.data.TaskEntityData;
import ru.timutkin.tdd.web.controller.data.UserEntityData;


import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    DepartmentRepository departmentRepository;


    @Test
    void createTask_TaskIsValid_ReturnsValidResponse() throws Exception {
        UserEntity user = UserEntityData.getFirstUserEntity();
        userRepository.save(user);
        TaskDto task = TaskDtoData.getFirstValidTaskDto();
        task.setUserId(user.getId());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = mapper.writeValueAsString(task);
        mvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "taskName":"API - POST : /api/v1/users",
                            "message": "Message",
                            "userId": 1
                        }
                        """)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json(json)
        );
    }

    @Test
    void createTask_TaskIsNonValidUserId_ReturnsBadRequest() throws Exception {
        mvc.perform(post(ApiConstant.VERSION_API + "/tasks")
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
        UserEntity userEntity = UserEntityData.getFirstUserEntity();
        userRepository.save(userEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        TaskEntity second = TaskEntityData.getValidTaskEntityList().get(1);
        first.setUser(userEntity);
        second.setUser(userEntity);
        taskRepository.save(first);
        taskRepository.save(second);
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "id": 1,
                                        "dataTimeOfCreation":"2023-03-03T00:00:00",
                                        "taskName":"task1",
                                        "message": "message",
                                        "status": "OPEN",
                                        "userId": 1
                                    },
                                    {
                                        "id": 2,
                                        "dataTimeOfCreation":"2023-03-03T00:00:00",
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

    @Test
    void updateTask_TaskIsValid_ReturnsValidResponseEntity() throws Exception {
        UserEntity firstUserEntity = UserEntityData.getFirstUserEntity();
        UserEntity secondUserEntity = UserEntityData.getListOfUserEntity().get(1);
        userRepository.save(firstUserEntity);
        userRepository.save(secondUserEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        first.setUser(firstUserEntity);
        taskRepository.save(first);
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {   
                                    "id":1,
                                    "taskName":"new task",
                                    "message": "new message",
                                    "dataTimeOfCreation": "2023-03-03T00:00",
                                    "status":"CLOSED",
                                    "userId": 2
                                }
                                """)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                   {
                                       "id": 1,
                                       "dataTimeOfCreation":"2023-03-03T00:00:00",
                                       "taskName":"new task",
                                       "message": "new message",
                                       "status": "CLOSED",
                                       "userId": 2
                                   }
                                """
                        )
                );
    }

    @Test
    void updateTask_TaskIsNonValid_ReturnsValidResponseEntity() throws Exception {
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {   
                                    "taskName":"new task",
                                    "message": "new message",
                                    "dataTimeOfCreation": "2023-03-03T00:00",
                                    "status":"CLOSED",
                                    "userId": 2
                                }
                                """)
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
        Long taskId = 1L;
        mvc.perform(delete(ApiConstant.VERSION_API + "/tasks/{taskID}", taskId)
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
                        content().json("""
                                {
                                   "id": 1,
                                   "dataTimeOfCreation":"2023-03-03T00:00:00",
                                   "taskName":"task1",
                                   "message": "message",
                                   "status": "OPEN",
                                   "userId": 1
                                }
                                """)

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