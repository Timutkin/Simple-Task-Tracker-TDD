package ru.timutkin.tdd.web.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.timutkin.tdd.dto.DepartmentDto;
import ru.timutkin.tdd.service.DepartmentService;
import ru.timutkin.tdd.web.constant.ApiConstant;
import ru.timutkin.tdd.web.validation.DepartmentControllerValidation;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@RestController
@RequestMapping(value = ApiConstant.VERSION_API + "/department",
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
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(departmentDto);
    }
}
