package ru.timutkin.tdd.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.timutkin.tdd.dto.DepartmentDto;
import ru.timutkin.tdd.exception.not_found.DepartmentNotFoundException;
import ru.timutkin.tdd.mapper.DepartmentMapper;
import ru.timutkin.tdd.service.DepartmentService;
import ru.timutkin.tdd.store.entity.DepartmentEntity;
import ru.timutkin.tdd.store.entity.UserEntity;
import ru.timutkin.tdd.store.repository.DepartmentRepository;
import ru.timutkin.tdd.store.repository.UserRepository;
import ru.timutkin.tdd.web.constant.ValidationConstant;

import static ru.timutkin.tdd.web.handler.error_objects.ApiValidationError.getApiValidationError;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

    DepartmentRepository departmentRepository;

    UserRepository userRepository;

    DepartmentMapper departmentMapper;

    @Override
    public DepartmentDto create(String name, Long headId) {
        DepartmentEntity departmentEntity = departmentMapper.fromParam(name);
        UserEntity user = userRepository.findById(headId).orElseThrow(
                () -> new DepartmentNotFoundException(
                        getApiValidationError(headId,
                                ValidationConstant.USER_WITH_ID_NOT_FOUND.formatted(headId),"headId", headId)
                )
        );
        departmentEntity.setDepartmentHead(user);
        departmentRepository.save(departmentEntity);
        return departmentMapper.departmentEntityToDepartmentDto(departmentEntity);
    }
}
