package ru.timutkin.tdd.web.controller;

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

    @PostMapping
    public ResponseEntity<ProjectDto> create(@RequestParam(name = "name") String name,
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

}