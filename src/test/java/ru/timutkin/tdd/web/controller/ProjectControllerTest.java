package ru.timutkin.tdd.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.timutkin.tdd.dto.ProjectDto;
import ru.timutkin.tdd.exception.validation.IncorrectFieldException;
import ru.timutkin.tdd.exception.validation.IncorrectPathVariableException;
import ru.timutkin.tdd.exception.validation.IncorrectRequestParamException;
import ru.timutkin.tdd.service.ProjectService;
import ru.timutkin.tdd.web.controller.data.ProjectDtoData;
import ru.timutkin.tdd.web.controller.data.TaskDtoData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    ProjectService projectService;

    @InjectMocks
    ProjectController projectController;

    @Test
    void createProject_DataIsValid_ReturnsValidResponse() {
        String name = "Amazing project";
        Long userHead = 1L;
        when(projectService.create(name, userHead))
                .thenReturn(ProjectDtoData.getFirstProjectDto());

        var request = projectController.createProject(name, userHead);

        assertAll(
                () -> assertNotNull(request),
                () -> assertEquals(HttpStatus.CREATED, request.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, request.getHeaders().getContentType()),
                () -> assertEquals(ProjectDtoData.getFirstProjectDto(), request.getBody())
        );
    }

    @Test
    void createProject_NameIsNonValid_ThrowsException() {
        String name = "   ";
        Long userHead = ProjectDtoData.getFirstProjectDto().getUserHead();
        assertThrows(IncorrectRequestParamException.class, () -> projectController.createProject(name, userHead));
    }

    @Test
    void createProject_UserHeadIsNonValid_ThrowsException() {
        String name = ProjectDtoData.getFirstProjectDto().getName();
        Long userHead = -1L;
        assertThrows(IncorrectRequestParamException.class, () -> projectController.createProject(name, userHead));
    }

    @Test
    void findById_ProjectIdIsValid_ReturnsValidResponse() {
        Long id = 1L;
        ProjectDto expectedProject = ProjectDtoData.getFirstProjectDto();
        when(projectService.findById(id)).thenReturn(expectedProject);

        var request = projectController.findById(id);
        assertAll(
                () -> assertNotNull(request),
                () -> assertEquals(HttpStatus.OK, request.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, request.getHeaders().getContentType()),
                () -> assertEquals(expectedProject, request.getBody())
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -3L})
    @NullSource
    void findById_ProjectIdIsNonValid_ThrowsException(Long projectId) {
        assertThrows(IncorrectPathVariableException.class,  ()-> projectController.findById(projectId));
    }


    @Test
    void deleteById_ProjectIdIsValid_ReturnsValidResponse(){
        Long projectId = 1L;
        var response = projectController.deleteById(projectId);
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType()),
                () -> assertEquals(projectId, response.getBody())
        );
        Mockito.verify(this.projectService, times(1)).deleteById(projectId);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -3L})
    @NullSource
    void deleteById_ProjectIdIsNonValid_ThrowsIncorrectPathVariableException(Long taskId) {
        assertThrows(IncorrectPathVariableException.class, () -> projectController.deleteById(taskId));
    }

    @Test
    void update_ProjectDtoIsValid_ReturnsValidResponse(){
        ProjectDto updatedProjectDto = ProjectDto.builder().id(1L).name("New project").build();
        ProjectDto projectDto = ProjectDtoData.getFirstProjectDto();
        projectDto.setName("New project");
        when(projectService.update(updatedProjectDto)).thenReturn(projectDto);

        var response = projectController.update(updatedProjectDto);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType()),
                () -> assertEquals(projectDto, response.getBody())
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    @NullSource
    void update_ProjectIdIsNonValid_ThrowsException(Long projectId){
        ProjectDto updatedProjectDto = ProjectDtoData.getFirstProjectDto();
        updatedProjectDto.setId(projectId);
        assertThrows(IncorrectFieldException.class, () -> projectController.update(updatedProjectDto));
    }

    @Test
    void update_ProjectNameIsBlank_ThrowsException(){
        ProjectDto updatedProjectDto = ProjectDtoData.getFirstProjectDto();
        updatedProjectDto.setName("   ");
        assertThrows(IncorrectFieldException.class, () -> projectController.update(updatedProjectDto));
    }

    @Test
    void update_TaskIdIsNonNull_ThrowsException(){
        ProjectDto updatedProjectDto = ProjectDtoData.getFirstProjectDto();
        updatedProjectDto.setTasksId(List.of(TaskDtoData.getFirstValidTaskDto().getId()));
        assertThrows(IncorrectFieldException.class, () -> projectController.update(updatedProjectDto));
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    @NullSource
    void update_UserHeadIsNonValid_ThrowsException(Long projectId){
        ProjectDto updatedProjectDto = ProjectDtoData.getFirstProjectDto();
        updatedProjectDto.setUserHead(projectId);
        assertThrows(IncorrectFieldException.class, () -> projectController.update(updatedProjectDto));
    }


}