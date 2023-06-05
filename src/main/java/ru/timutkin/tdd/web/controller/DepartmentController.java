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
import ru.timutkin.tdd.dto.DepartmentDto;
import ru.timutkin.tdd.service.DepartmentService;
import ru.timutkin.tdd.web.constant.ApiConstant;
import ru.timutkin.tdd.web.constant.SwaggerDescription;
import ru.timutkin.tdd.web.constant.ValidationConstant;
import ru.timutkin.tdd.web.handler.error_objects.ApiError;
import ru.timutkin.tdd.web.validation.DepartmentControllerValidation;

@Tag(name = "Department management APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@RestController
@RequestMapping(value = ApiConstant.VERSION_API + "/departments",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class DepartmentController {


    DepartmentService departmentService;

    @Operation(summary = "Create an Department",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "The department was successfully created "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error: the department name is empty, the headId is not found. "
                    ),
            })
    @PostMapping
    public ResponseEntity<DepartmentDto> create(@RequestParam(name = "name") String name,
                                                @RequestParam(name = "departmentHead") Long headId
    ) {
        DepartmentControllerValidation.validateCreate(name, headId);
        DepartmentDto departmentDto = departmentService.create(name, headId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(departmentDto);
    }

    @Operation(summary = "Get an department",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The department was successfully received "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : " + ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0
                    )})
    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentDto> findById(@PathVariable Long departmentId){
        DepartmentControllerValidation.validateId(departmentId);
        DepartmentDto departmentDto = departmentService.findById(departmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(departmentDto);
    }

    @Operation(summary = "Delete an department",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The department was successfully deleted "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : " + ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0
                    )})
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Long> deleteById(@PathVariable Long departmentId){
        DepartmentControllerValidation.validateId(departmentId);
        departmentService.deleteById(departmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(departmentId);
    }

    @Operation(summary = "Update an department", description = SwaggerDescription.UPDATE_TASK,
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The department was successfully updated "
                    ),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class)),
                            description = "Validation error : department id not found or <= 0, department name is blank, head id <= 0 or not found."
                    ),
            })
    @PutMapping
    public ResponseEntity<DepartmentDto> updateDepartment(@RequestBody DepartmentDto departmentDto){
        DepartmentControllerValidation.validateUpdate(departmentDto);
        DepartmentDto updatedDepartmentDto = departmentService.update(departmentDto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedDepartmentDto);
    }
}
