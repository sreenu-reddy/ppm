package com.sree.ppm.services;


import com.sree.ppm.exceptions.ProjectIdException;
import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.domains.Project;
import com.sree.ppm.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    ProjectRepository projectRepository;
    ProjectService projectService;

    @Captor
    ArgumentCaptor<String> identifierCapture;


    @BeforeEach
    void setUp() {
        projectService = new ProjectServiceImpl(projectRepository);
    }


    @Test
    void createNewProject() {
//        given
        Project project = new Project();
        project.setId(1L);
        project.setProjectName("ProjectName");
        project.setProjectIdentifier("Identifier");
        project.setDescription("Description");

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setProjectIdentifier(project.getProjectIdentifier());
        projectDTO.setDescription(project.getDescription());

        given(projectRepository.save(any(Project.class))).willReturn(project);
//        When
        ProjectDTO projectDTO1 = projectService.createNewProject(projectDTO);

//        then
        assertNotNull(projectDTO1);
        assertEquals(1L,projectDTO1.getId());
        assertEquals("ProjectName",projectDTO1.getProjectName());
        assertEquals("Identifier",projectDTO1.getProjectIdentifier());
        assertEquals("Description",projectDTO1.getDescription());
        assertNull(projectDTO1.getStartDate());
        assertNull(projectDTO1.getEndDate());
    }

    @Test
    void createNewProjectThrowsProjectIdExp(){
//        given
        Project project = new Project();
        project.setId(1L);
        project.setProjectName("ProjectName");
        project.setProjectIdentifier("Identifier");
        project.setDescription("Description");
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setProjectIdentifier(project.getProjectIdentifier());
        projectDTO.setDescription(project.getDescription());
        given(projectRepository.save(any(Project.class))).willThrow(ProjectIdException.class);
//     When
        assertThrows(ProjectIdException.class,()->projectService.createNewProject(projectDTO));

    }

    @Test
    void getProjectByIdentifier(){
//        Given
        Project project = new Project();
        project.setId(1L);
        project.setProjectName("ProjectName");
        project.setProjectIdentifier("Identifier");
        project.setDescription("Description");
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setProjectIdentifier(project.getProjectIdentifier());
        projectDTO.setDescription(project.getDescription());
        given(projectRepository.findByProjectIdentifier(identifierCapture.capture())).willReturn(project);
//        When
        ProjectDTO projectDTO1 = projectService.getProjectByIdentifier("Identifier");
//        Then
        assertNotNull(projectDTO1);
        assertEquals(1L,projectDTO1.getId());
        assertEquals("ProjectName",projectDTO1.getProjectName());
        assertNull(projectDTO1.getStartDate());
        then(projectRepository).should().findByProjectIdentifier(identifierCapture.getValue());
        assertEquals("IDENTIFIER",identifierCapture.getValue());
        then(projectRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void getProjectByIdentifierThrowsProjectIdExp(){
//        Given
        given(projectRepository.findByProjectIdentifier(any(String.class))).willReturn(nullable(Project.class));

//        Then
        assertThrows(ProjectIdException.class,()->projectService.getProjectByIdentifier(""));

    }


}