package ru.timutkin.tdd.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.tdd.dto.DepartmentDto;
import ru.timutkin.tdd.exception.not_found.DepartmentNotFoundException;
import ru.timutkin.tdd.exception.not_found.UserNotFoundException;
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
    @Retryable(maxAttempts = 2)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public DepartmentDto create(String name, Long headId) {
        DepartmentEntity departmentEntity = departmentMapper.fromParam(name);
        UserEntity user = userRepository.findById(headId).orElseThrow(
                () -> new UserNotFoundException(
                        getApiValidationError(headId,
                                ValidationConstant.USER_WITH_ID_NOT_FOUND.formatted(headId),"headId", headId)
                )
        );
        departmentEntity.setDepartmentHead(user);
        departmentRepository.save(departmentEntity);
        return departmentMapper.departmentEntityToDepartmentDto(departmentEntity);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public DepartmentDto findById(Long departmentId) {
        return departmentMapper.departmentEntityToDepartmentDto(
                departmentRepository.findById(departmentId).orElseThrow(
                        () -> new DepartmentNotFoundException(
                                getApiValidationError(departmentId,
                                        ValidationConstant.DEPARTMENT_WITH_ID_NOT_FOUND.formatted(departmentId),"departmentId", departmentId )
                        )
                )
        );
    }

    @Override
    @Retryable(maxAttempts = 2)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteById(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)){
            throw new DepartmentNotFoundException(
                    getApiValidationError(departmentId,
                            ValidationConstant.DEPARTMENT_WITH_ID_NOT_FOUND.formatted(departmentId),"departmentId", departmentId )
            );
        }
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public DepartmentDto update(DepartmentDto departmentDto) {
        DepartmentEntity updatedDepartmentEntity = departmentRepository.findById(departmentDto.getId())
                .orElseThrow(
                        () -> new DepartmentNotFoundException(
                                getApiValidationError(departmentDto.getId(),
                                        ValidationConstant.DEPARTMENT_WITH_ID_NOT_FOUND.formatted(departmentDto.getId()),
                                        "departmentId", departmentDto.getId() )
                        )
                );
        departmentMapper.updateDepartmentEntityFromDepartmentDto(departmentDto, updatedDepartmentEntity);
        return departmentMapper.departmentEntityToDepartmentDto(updatedDepartmentEntity);
    }


}
