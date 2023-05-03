package ru.timutkin.tdd.web.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.tdd.dto.CreationTaskRequest;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.store.entity.ProjectEntity;
import ru.timutkin.tdd.store.entity.TaskEntity;
import ru.timutkin.tdd.store.entity.UserEntity;
import ru.timutkin.tdd.mapper.TaskMapper;
import ru.timutkin.tdd.store.repository.ProjectRepository;
import ru.timutkin.tdd.store.repository.TaskRepository;
import ru.timutkin.tdd.store.repository.UserRepository;
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

/**
 * The type Task controller it.
 * This class testing all endpoints : /api/v1/tasks
 *
 * @author Timofey Utkin
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class TaskControllerIT {


    @Autowired
    MockMvc mvc;
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;
    /**
     * The Json converter.
     * Converts Java objects to json
     */
    @Autowired
    JsonConverter jsonConverter;

    @Autowired
    TaskMapper taskMapper;

    /**
     * Testing:
     * POST : /api/v1/tasks
     * body : CreationTaskRequest
     * Should return valid response - HTTP code 201
     * and correct body
     *
     * @throws Exception the exception
     */
    @Test
    void createTask_TaskIsValid_ReturnsValidResponse() throws Exception {
        /*
        The task is assigned to a user who exists
         */
        UserEntity user = UserEntityData.getFirstUserEntity();
        userRepository.save(user);
        /*
        Generate expected object - TaskDto task
         */
        TaskDto task = TaskDtoData.getFirstValidTaskDto();
        task.setUserId(user.getId());
        task.setCreatedAt(DateFormatHM.getDateTime());
        /*
        Usually the task refers to a specific project, therefore a project is created
         */
        projectRepository.save(ProjectEntity
                .builder()
                .name("Amazing project")
                .build());
        /*
        Setting a project to a task object
         */
        task.setProjectId(1L);
        /*
        Generate expected body of response
         */
        String jsonTask = jsonConverter.convert(task);
        /*
        Generate creation request
         */
        CreationTaskRequest request = new CreationTaskRequest(task.getTaskName(), task.getMessage(), task.getUserId(), task.getProjectId());
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

    /**
     * Testing:
     * POST : /api/v1/tasks
     * body : CreationTaskRequest with incorrect field : isBlank, null, id <=0 or not found
     * Should return valid response - HTTP code 400
     * The response body is not being tested
     *
     * @throws Exception the exception
     */
    @Test
    void createTask_TaskIsNonValidUserId_ReturnsBadRequest() throws Exception {
        /*
        Creating exceptional situations
         */
        CreationTaskRequest first = new CreationTaskRequest("   ", "message", 1L, 1L);
        CreationTaskRequest second = new CreationTaskRequest("task name", " ", 1L, 1L);
        CreationTaskRequest third = new CreationTaskRequest("task name", "message", 1L, 1L);
        CreationTaskRequest fourth = new CreationTaskRequest("task name", "message", 1L, 1L);
        CreationTaskRequest fifth = new CreationTaskRequest("task name", "message", -1L, 1L);
        CreationTaskRequest sixth = new CreationTaskRequest("task name", "message", 1L, -1L);
        /*
        Should return BAD_REQUEST because taskName is blank
         */
        mvc.perform(post(ApiConstant.VERSION_API + "/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convert(first))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
         /*
        Should return BAD_REQUEST because message is blank
         */
        mvc.perform(post(ApiConstant.VERSION_API + "/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convert(second))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
            /*
        Should return BAD_REQUEST because userId <= 0
         */
        mvc.perform(post(ApiConstant.VERSION_API + "/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convert(fifth))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
         /*
        Should return BAD_REQUEST because userId is not found
         */
        mvc.perform(post(ApiConstant.VERSION_API + "/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convert(third))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
         /*
        We save the user, otherwise we will get a BAD_REQUEST for this
        request due to the absence of the user in the database
         */
        userRepository.save(UserEntityData.getFirstUserEntity());
        /*
        Should return BAD_REQUEST because projectId is not found
         */
        mvc.perform(post(ApiConstant.VERSION_API + "/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convert(fourth))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
         /*
        Should return BAD_REQUEST because projectId <= 0
         */
        mvc.perform(post(ApiConstant.VERSION_API + "/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.convert(sixth))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
        );
    }

    /**
     * Testing:
     * GET : /api/v1/tasks
     * Find all tasks when none of the request parameters were passed.
     * Should return valid response - HTTP code 200 and correct body
     *
     * @throws Exception the exception
     */
    @Test
    void findAll_WithoutRequestParam_ReturnValidResponseEntity() throws Exception {
        /*
        Filling the database with values :  userEntity - user for all tasks
                                            first - task, which will be in the database
                                            second - task, which will be in the database
         */
        UserEntity userEntity = UserEntityData.getFirstUserEntity();
        userRepository.save(userEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        TaskEntity second = TaskEntityData.getSecondValidTaskEntity();
        first.setUser(userEntity);
        second.setUser(userEntity);
        taskRepository.save(first);
        taskRepository.save(second);
        /*
        Generate expected values
         */
        List<TaskDto> taskEntityList = List.of(taskMapper.taskEntityToTaskDto(first), taskMapper.taskEntityToTaskDto(second));
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonConverter.convert(taskEntityList)
                        )
                );
    }

    /**
     * Testing:
     * GET : /api/v1/tasks
     * Find all tasks when parameters passed : after, before .
     * Should return valid response - HTTP code 200 and correct body
     *
     * @throws Exception the exception
     */
    @Test
    void findAll_RequestParamAfterBefore_ReturnsValidResponseEntity() throws Exception {
        /*
        Filling the database with values :  userEntity - user for all tasks
                                            first - task, which will be in the database
                                            second - task, which will be in the database
         */
        UserEntity userEntity = UserEntityData.getFirstUserEntity();
        userRepository.save(userEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        TaskEntity second = TaskEntityData.getSecondValidTaskEntity();
        first.setUser(userEntity);
        second.setUser(userEntity);
        taskRepository.save(first);
        taskRepository.save(second);
        /*
        Should return task that created after this date : 2023-03-02T00:00
        expected : first, second
         */
        List<TaskDto> taskEntityList = List.of(taskMapper.taskEntityToTaskDto(first), taskMapper.taskEntityToTaskDto(second));
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("after", "2023-03-02T00:00"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskEntityList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
          /*
        Should return task that created after this date : 2023-03-05T00:00
        expected : []
         */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("after", "2023-03-05T00:00"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return task that created before this date : 2023-03-04T00:00
        expected : first, second
         */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("before", "2023-03-04T00:00"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskEntityList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return task that created before this date : 2023-03-02T00:00
        expected : []
         */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("before", "2023-03-02T00:00"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return task that created from interval : 2023-03-02T00:00 <= date <= 2023-03-04T00:00
        expected : first, second
         */
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

    /**
     * Testing:
     * GET : /api/v1/tasks
     * Find all tasks when parameters passed : taskName, message .
     * Should return valid response - HTTP code 200 and correct body
     *
     * @throws Exception the exception
     */
    @Test
    void findAll_RequestParamTaskNameMessage_ReturnsValidResponseEntity() throws Exception {
        /*
        Filling the database with values :  userEntity - user for all tasks
                                            first - task, which will be in the database
                                            second - task, which will be in the database
         */
        UserEntity userEntity = UserEntityData.getFirstUserEntity();
        userRepository.save(userEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        TaskEntity second = TaskEntityData.getSecondValidTaskEntity();
        first.setUser(userEntity);
        second.setUser(userEntity);
        taskRepository.save(first);
        taskRepository.save(second);
        List<TaskDto> taskDtoList = List.of(taskMapper.taskEntityToTaskDto(first), taskMapper.taskEntityToTaskDto(second));
        /*
        Should return task that taskName contains : "sk"
        expected : first, second
        */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("taskName", "sk"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskDtoList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return task that taskName contains : "www"
        expected : []
        */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("taskName", "www"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
         /*
        Should return task that message contains : "ssa"
        expected : first, second
        */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("message", "ssa"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskDtoList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return task that message contains : "www"
        expected : []
        */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("message", "www"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return task that message contains "www" and taskName contains "2"
        expected : second
         */
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

    /**
     * Testing:
     * GET : /api/v1/tasks
     * Find all tasks when parameters passed : status, message .
     * Should return valid response - HTTP code 200 and correct body
     *
     * @throws Exception the exception
     */
    @Test
    void findAll_RequestParamStatusUserId_ReturnsValidResponseEntity() throws Exception {
        UserEntity userEntity = UserEntityData.getFirstUserEntity();
        userRepository.save(userEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        TaskEntity second = TaskEntityData.getSecondValidTaskEntity();
        first.setUser(userEntity);
        second.setUser(userEntity);
        taskRepository.save(first);
        taskRepository.save(second);
        List<TaskDto> taskDtoList = List.of(taskMapper.taskEntityToTaskDto(first), taskMapper.taskEntityToTaskDto(second));
        /*
        Should return task that status is OPEN
        expected : first, second
        */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("status", "OPEN"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskDtoList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
          /*
        Should return task that status is CLOSED
        expected : []
        */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("status", "CLOSED"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
             /*
        Should return task that status is OPEN and userId is 1L
        expected : first, second
        */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks")
                        .param("status", "OPEN")
                        .param("userId", String.valueOf(1L))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskDtoList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return task that status is OPEN and userId is 2L
        expected : []
        */
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

    /**
     * Testing:
     * GET : /api/v1/tasks
     * Find all tasks when parameters passed : projectId.
     * Should return valid response - HTTP code 200 and correct body
     *
     * @throws Exception the exception
     */
    @Test
    void findAll_RequestParamProjectId_ReturnsValidResponseEntity() throws Exception {
        /*
        Filling the database with values :  userEntity - user for all tasks
                                            first - task, which will be in the database
                                            second - task, which will be in the database
                                            project
         */
        UserEntity userEntity = UserEntityData.getFirstUserEntity();
        userRepository.save(userEntity);
        TaskEntity first = TaskEntityData.getFirstValidTaskEntity();
        TaskEntity second = TaskEntityData.getSecondValidTaskEntity();
        first.setUser(userEntity);
        second.setUser(userEntity);
        taskRepository.save(first);
        taskRepository.save(second);
        ProjectEntity project = ProjectEntity
                .builder()
                .name("Amazing project")
                .build();
        projectRepository.save(project);
        first.setProject(project);
        second.setProject(project);
        taskRepository.save(first);
        taskRepository.save(second);
        List<TaskDto> taskDtoList = List.of(taskMapper.taskEntityToTaskDto(first), taskMapper.taskEntityToTaskDto(second));
        /*
        Should return task that projectId is 1
        expected : first, second
        */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("projectId", String.valueOf(project.getId())))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(taskDtoList)),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
          /*
        Should return task that projectId is 2
        expected : []
        */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("projectId", String.valueOf(2L)))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonConverter.convert(Collections.emptyList())),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    /**
     * Testing:
     * POST : /api/v1/tasks
     * params : after, before, taskName, message, userId, projectId
     * Should return valid response - HTTP code 400
     * The response body is not being tested
     *
     * @throws Exception the exception
     */
    @Test
    void findAll_RequestParamIsNonValid_ReturnValidResponseEntity() throws Exception {
        /*
        Should return BAD_REQUEST because after format is non valid
         */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("after", LocalDateTime.now().toString()))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return BAD_REQUEST because before format is non valid
         */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("before", LocalDateTime.now().toString()))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
          /*
        Should return BAD_REQUEST because taskName is blank
         */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("taskName", ""))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return BAD_REQUEST because message is blank
         */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("message", ""))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return BAD_REQUEST because status is not OPEN
         */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("status", "open"))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
            /*
        Should return BAD_REQUEST because userId <= 0
         */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("userId", String.valueOf(-1L)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return BAD_REQUEST because userId not found
         */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks").param("userId", String.valueOf(10L)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    /**
     * Testing:
     * PUT : /api/v1/tasks
     * body : TaskDto
     * Should return valid response - HTTP code 200 and valid response
     *
     * @throws Exception the exception
     */
    @Test
    void updateTask_TaskIsValid_ReturnsValidResponseEntity() throws Exception {
        /*
        Filling the database with values :  firstUserEntity - user for first tasks
                                            secondUserEntity - user for update
                                            task - task to update
                                            firstProject - project got firstTask
                                            secondProject - project to update
         */
        UserEntity firstUserEntity = UserEntityData.getFirstUserEntity();
        UserEntity secondUserEntity = UserEntityData.getSecondUserEntity();
        userRepository.save(firstUserEntity);
        userRepository.save(secondUserEntity);
        TaskEntity task = TaskEntityData.getFirstValidTaskEntity();
        ProjectEntity firstProject = ProjectEntity
                .builder()
                .name("Amazing project")
                .build();
        ProjectEntity secondProject = ProjectEntity
                .builder()
                .name("Awesome project")
                .build();
        projectRepository.save(firstProject);
        projectRepository.save(secondProject);
        task.setUser(firstUserEntity);
        task.setProject(firstProject);
        taskRepository.save(task);
        /*
        expected value : updatedTaskDto
         */
        TaskDto updatedTaskDto = new TaskDto(1L, DateFormatHM.allTime, "new task",
                "new message", "CLOSED", secondUserEntity.getId(), secondProject.getId());
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

    /**
     * Testing:
     * PUT : /api/v1/tasks
     * body : TaskDto with incorrect field : id is null,
     * string filed is blank,
     * id filed not found or <=0
     * Should return valid response - HTTP code 400
     * The response body is not being tested
     *
     * @throws Exception the exception
     */
    @Test
    void updateTask_TaskIsNonValid_ReturnsValidResponseEntity() throws Exception {
        TaskDto first = TaskDto.builder().id(0L).build();
        TaskDto second = TaskDto.builder().id(1L).createdAt(LocalDateTime.now()).build();
        TaskDto third = TaskDto.builder().id(1L).taskName("  ").build();
        TaskDto fourth = TaskDto.builder().id(1L).message(" ").build();
        TaskDto fifth = TaskDto.builder().id(1L).status("open").build();
        TaskDto sixth = TaskDto.builder().id(1L).userId(0L).build();
        TaskDto seventh = TaskDto.builder().id(1L).userId(-1L).build();
        TaskDto eighth = TaskDto.builder().id(1L).userId(1L).projectId(5L).build();
        TaskDto ninth = TaskDto.builder().id(1L).userId(1L).projectId(-1L).build();

        /*
        Should return BAD_REQUEST because id is null
         */
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(first))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        TaskEntity task = TaskEntityData.getFirstValidTaskEntity();
        UserEntity user = UserEntityData.getFirstUserEntity();
        userRepository.save(user);
        task.setUser(user);
        taskRepository.save(task);
          /*
        Should return BAD_REQUEST because createdAt format is non valid
         */
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(second))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return BAD_REQUEST because taskName is blank
         */
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(third))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return BAD_REQUEST because message is blank
         */
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(fourth))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return BAD_REQUEST because status is open (correct : OPEN)
         */
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(fifth))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
          /*
        Should return BAD_REQUEST because userId <= 0
         */
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(sixth))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
          /*
        Should return BAD_REQUEST because userId not found
         */
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(seventh))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return BAD_REQUEST because projectId not found
        */
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(eighth))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
          /*
        Should return BAD_REQUEST because projectId <= 0
        */
        mvc.perform(put(ApiConstant.VERSION_API + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConverter.convert(ninth))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    /**
     * Testing:
     * DELETE : /api/v1/tasks
     * path variable : taskId
     * Should return valid response - HTTP code 200 and valid response
     * @throws Exception the exception
     */
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

    /**
     * PUT : /api/v1/tasks
     * path variable : projectId - <=0 or not found
     * Should return valid response - HTTP code 400
     * The response body is not being tested
     * @throws Exception the exception
     */
    @Test
    void deleteById_TaskIdIsNonValid_ReturnsBadRequest() throws Exception {
        Long firstTaskId = 1L;
        Long secondTaskId = 0L;
        /*
        Should return BAD_REQUEST because projectId not found
        */
        mvc.perform(delete(ApiConstant.VERSION_API + "/tasks/{taskID}", firstTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return BAD_REQUEST because projectId <= 0
        */
        mvc.perform(delete(ApiConstant.VERSION_API + "/tasks/{taskID}", secondTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    /**
     * Testing:
     * GET : /api/v1/tasks
     * path variable : taskID
     * Find tasks by id.
     * Should return valid response - HTTP code 200 and correct body
     * @throws Exception the exception
     */
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

    /**
     * Testing:
     * GET : /api/v1/tasks
     * path variable : taskID not found
     * Find all tasks by id.
     * Should return valid response - HTTP code 400
     * The response body is not being tested
     * @throws Exception the exception
     */
    @Test
    void findById_TaskIdIsNonValid_ReturnsValidResponseEntity() throws Exception {
        Long taskId = 1L;
          /*
        Should return BAD_REQUEST because taskId not found
        */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        /*
        Should return BAD_REQUEST because taskId <= 0
        */
        mvc.perform(get(ApiConstant.VERSION_API + "/tasks/{taskId}", String.valueOf(-1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }
}