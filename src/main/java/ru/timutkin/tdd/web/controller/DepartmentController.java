package ru.timutkin.tdd.web.controller;

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
import ru.timutkin.tdd.web.validation.DepartmentControllerValidation;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@RestController
@RequestMapping(value = ApiConstant.VERSION_API + "/departments",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class DepartmentController {


    DepartmentService departmentService;

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

    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentDto> findById(@PathVariable Long departmentId){
        DepartmentControllerValidation.validateId(departmentId);
        DepartmentDto departmentDto = departmentService.findById(departmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(departmentDto);
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Long> deleteById(@PathVariable Long departmentId){
        DepartmentControllerValidation.validateId(departmentId);
        departmentService.deleteById(departmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(departmentId);
    }

    @PutMapping
    public ResponseEntity<DepartmentDto> updateDepartment(@RequestBody DepartmentDto departmentDto){
        DepartmentControllerValidation.validateUpdate(departmentDto);
        DepartmentDto updatedDepartmentDto = departmentService.update(departmentDto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedDepartmentDto);
    }
}
