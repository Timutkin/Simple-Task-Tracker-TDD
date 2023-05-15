package ru.timutkin.tdd.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import ru.timutkin.tdd.web.handler.error_objects.ApiError;
import ru.timutkin.tdd.web.validation.ProjectControllerValidation;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@RestController
@RequestMapping(value = ApiConstant.VERSION_API + "/projects",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ProjectController {

    ProjectService projectService;

    @Operation(summary = "Create an project", description = SwaggerDescription.CREATE_PROJECT,
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The project was successfully created "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : project name is blank, user id or task id found "
                    )})
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestParam(name = "name") String name,
                                                    @RequestParam(name = "userHead", required = false) Long userHead,
                                                    @RequestParam(name = "tasksId", required = false) List<Long> tasksId
    ) {
        ProjectControllerValidation.validateCreate(name, userHead,tasksId);
        ProjectDto projectDto = projectService.create(name, userHead, tasksId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectDto);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> findById(@PathVariable Long projectId){
        ProjectControllerValidation.validateId(projectId);
        ProjectDto projectDto = projectService.findById(projectId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectDto);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Long> deleteById(@PathVariable Long projectId){
        ProjectControllerValidation.validateId(projectId);
        projectService.deleteById(projectId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectId);
    }

    @PutMapping
    public ResponseEntity<ProjectDto> update(@RequestBody ProjectDto projectDto){
        ProjectControllerValidation.validateUpdate(projectDto);
        ProjectDto updatedProjectDto = projectService.update(projectDto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedProjectDto);
    }

}
