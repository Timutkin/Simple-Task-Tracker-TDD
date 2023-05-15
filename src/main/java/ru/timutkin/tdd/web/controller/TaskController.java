package ru.timutkin.tdd.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.timutkin.tdd.dto.CreationTaskRequest;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.dto.param.FilterTaskParams;
import ru.timutkin.tdd.service.TaskService;
import ru.timutkin.tdd.web.constant.ApiConstant;
import ru.timutkin.tdd.web.constant.SwaggerDescription;
import ru.timutkin.tdd.web.constant.ValidationConstant;
import ru.timutkin.tdd.web.handler.error_objects.ApiError;
import ru.timutkin.tdd.web.validation.TaskControllerValidation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Tag(name = "Task management APIs")
@RestController
@AllArgsConstructor
@RequestMapping(
        value = ApiConstant.VERSION_API + "/tasks",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create an task", description = SwaggerDescription.CREATE_TASK,
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The task was successfully created "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error: the task name or message is empty, the user or project ID is not found, " +
                                          "or the task with the name and message already exists "
                    ),
                    @ApiResponse(responseCode = "409",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Task with name and message already exists"
                    ),
            })
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody CreationTaskRequest request) {
        TaskControllerValidation.validateCreate(request);
        TaskDto task = taskService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
    }

    @Operation(summary = "Update an task", description = SwaggerDescription.UPDATE_TASK,
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The task was successfully updated "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : task id not found, task name or message is blank, user id or project id not found, " +
                                          "incorrect data format (valid format:uuuu-MM-dd'T'HH:mm), status in non valid " +
                                          "(acceptable values: OPEN, IN_PROGRESS, RESOLVED, REOPENED, CLOSED)."
                    )})
    @PutMapping
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto) {
        TaskControllerValidation.validateUpdate(taskDto);
        TaskDto updatedTaskDto = taskService.update(taskDto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedTaskDto);
    }

    @Operation(summary = "Get an task",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The task was successfully received "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : " + ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0
                    )})
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> findById(@PathVariable Long taskId) {
        TaskControllerValidation.validatePathVariableAndRequestParamId(taskId);
        TaskDto task = taskService.findById(taskId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
    }

    @Operation(summary = "Get a list of tasks satisfying the filter",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The list of tasks was successfully received "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : task id not found, task name or message is blank, user id or project id not found, " +
                                          "incorrect data format (valid format:uuuu-MM-dd'T'HH:mm), status in non valid " +
                                          "(acceptable values: OPEN, IN_PROGRESS, RESOLVED, REOPENED, CLOSED)."
                    )})
    @GetMapping
    public ResponseEntity<List<TaskDto>> findAllByParam(@RequestParam(name = "after") Optional<LocalDateTime> after,
                                                        @RequestParam(name = "before") Optional<LocalDateTime> before,
                                                        @RequestParam(name = "taskName") Optional<String> taskName,
                                                        @RequestParam(name = "message") Optional<String> message,
                                                        @RequestParam(name = "status") Optional<String> status,
                                                        @RequestParam(name = "userId") Optional<Long> userId,
                                                        @RequestParam(name = "projectId") Optional<Long> projectId
    ) {
        FilterTaskParams filterParams = new FilterTaskParams(after, before, taskName, message, status, userId, projectId);
        TaskControllerValidation.validateFilter(filterParams);
        List<TaskDto> taskDtoList = taskService.findByParam(filterParams);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskDtoList);
    }

    @Operation(summary = "Get a list of tasks assigned to the user",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The list of tasks was successfully received "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : " + ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0
                    )})
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDto>> findListOfTaskEntityByUserId(@PathVariable Long userId) {
        TaskControllerValidation.validatePathVariableAndRequestParamId(userId);
        List<TaskDto> taskDtoList = taskService.findListOfTaskByUserId(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskDtoList);
    }

    @Operation(summary = "Delete an task",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The tasks was successfully deleted "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : " + ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0
                    )})
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Long> deleteById(@PathVariable Long taskId) {
        TaskControllerValidation.validatePathVariableAndRequestParamId(taskId);
        taskService.deleteById(taskId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskId);
    }
}
