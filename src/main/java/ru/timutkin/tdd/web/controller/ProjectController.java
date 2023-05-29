package ru.timutkin.tdd.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.timutkin.tdd.dto.ProjectDto;
import ru.timutkin.tdd.service.ProjectService;
import ru.timutkin.tdd.web.constant.ApiConstant;
import ru.timutkin.tdd.web.constant.SwaggerDescription;
import ru.timutkin.tdd.web.constant.ValidationConstant;
import ru.timutkin.tdd.web.handler.error_objects.ApiError;
import ru.timutkin.tdd.web.validation.ProjectControllerValidation;

@Tag(name = "Project management APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@RestController
@RequestMapping(value = ApiConstant.VERSION_API + "/projects",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ProjectController {

    ProjectService projectService;

    @Operation(summary = "Create an project",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The project was successfully created"
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error: the project name is blank, or the user id <="
                    ),
                    @ApiResponse(responseCode = "409",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Project with this name already exists"
                    ),
            })
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestParam(name = "name") String name,
                                                    @RequestParam(name = "userHead") Long userHead
    ) {
        ProjectControllerValidation.validateCreate(name, userHead);
        ProjectDto projectDto = projectService.create(name, userHead);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectDto);
    }

    @Operation(summary = "Get an project",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The project was successfully received "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : " + ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0
                    )})
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> findById(@PathVariable Long projectId){
        ProjectControllerValidation.validateId(projectId);
        ProjectDto projectDto = projectService.findById(projectId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectDto);
    }

    @Operation(summary = "Delete an task",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The project was successfully deleted "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : " + ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0
                    )})
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Long> deleteById(@PathVariable Long projectId){
        ProjectControllerValidation.validateId(projectId);
        projectService.deleteById(projectId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectId);
    }

    @Operation(summary = "Update an project", description = SwaggerDescription.UPDATE_TASK,
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The project was successfully updated "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : project id not found or <= 0, project name is blank, user id <= 0 or not found, " +
                                          "incorrect data format (valid format:uuuu-MM-dd'T'HH:mm)"
                    ),
                    @ApiResponse(responseCode = "409",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Project with this name already exists")
            })
    @PutMapping
    public ResponseEntity<ProjectDto> update(@RequestBody ProjectDto projectDto){
        ProjectControllerValidation.validateUpdate(projectDto);
        ProjectDto updatedProjectDto = projectService.update(projectDto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedProjectDto);
    }
}
