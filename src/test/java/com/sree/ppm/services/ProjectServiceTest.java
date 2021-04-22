package com.sree.ppm.services;


import com.sree.ppm.Exceptions.ProjectIdException;
import com.sree.ppm.domains.Project;
import com.sree.ppm.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    ProjectRepository projectRepository;
    ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectService = new ProjectServiceImpl(projectRepository);
    }

    @Test
    void createNewProjectThrowsNullExp(){
        assertThrows(NullPointerException.class,()->projectService.createNewProject(null));
    }


    @Test
    void createNewProject() {
//        given
        Project project = new Project();
        project.setId(1L);
        project.setProjectName("ProjectName");
        project.setProjectIdentifier("Identifier");
        project.setDescription("Description");

        given(projectRepository.save(any(Project.class))).willReturn(project);
//        When
        Project project1 = projectService.createNewProject(project);

//        then
        assertNotNull(project1);
        assertEquals(1L,project1.getId());
        assertNull(project1.getStart_date());
    }

    @Test
    void createNewProjectThrowsProjectIdExp(){
//        given
        Project project = new Project();
        project.setId(1L);
        project.setProjectName("ProjectName");
        project.setProjectIdentifier("Identifier");
        project.setDescription("Description");
        given(projectRepository.save(any(Project.class))).willThrow(ProjectIdException.class);
//     When
        assertThrows(ProjectIdException.class,()->projectService.createNewProject(project));

    }



}