package com.sree.ppm.services;


import com.sree.ppm.api.v1.models.ProjectListDTO;
import com.sree.ppm.domains.BackLog;
import com.sree.ppm.exceptions.ProjectIdException;
import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.domains.Project;
import com.sree.ppm.repositories.BackLogRepository;
import com.sree.ppm.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    ProjectRepository projectRepository;
    @Mock
    BackLogRepository backLogRepository;

    ProjectService projectService;

    @Captor
    ArgumentCaptor<String> identifierCapture;


    @BeforeEach
    void setUp() {
        projectService = new ProjectServiceImpl(projectRepository, backLogRepository);
    }


    @Test
    void createNewProject() {
//        given
        Project project = new Project();
        project.setId(1L);
        project.setProjectName("ProjectName");
        project.setProjectIdentifier("Identifier");
        project.setDescription("Description");
        BackLog backLog = new BackLog();
        backLog.setId(1L);
        backLog.setProject(project);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        project.setBackLog(backLog);

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setProjectIdentifier(project.getProjectIdentifier());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setBackLog(project.getBackLog());

        given(projectRepository.save(any(Project.class))).willReturn(project);
//        When
        ProjectDTO projectDTO1 = projectService.createNewProject(projectDTO);

//        then
        assertNotNull(projectDTO1);
        assertEquals(1L,projectDTO1.getId());
        assertEquals("ProjectName",projectDTO1.getProjectName());
        assertEquals("Identifier",projectDTO1.getProjectIdentifier());
        assertEquals("Description",projectDTO1.getDescription());
        assertEquals(1L,projectDTO1.getBackLog().getId());
        assertEquals(project.getProjectIdentifier(),projectDTO1.getBackLog().getProjectIdentifier());
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

    @Test
    void getAllProjects(){
//        Given
        List<Project> projectList = new ArrayList<>();
        Project project = new Project();
        project.setId(1L);
        project.setProjectName("Name");
        project.setProjectIdentifier("Iden");
        projectList.add(project);
        given(projectRepository.findAll()).willReturn(projectList);
//        When
        ProjectListDTO projects = projectService.getAllProjects();

//        Then
        assertNotNull(projects);
        assertEquals(1,projects.getProjects().size());
    }

    @Test
    void deleteProject(){
//        given
        Project project = new Project();
        project.setId(1L);
        project.setProjectName("ProjectName");
        project.setProjectIdentifier("Identifier");
        project.setDescription("Description");
        given(projectRepository.findByProjectIdentifier(any())).willReturn(project);
//        When
        projectService.deleteProject(project.getProjectIdentifier());

//        then
        then(projectRepository).should().delete(project);
        then(projectRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteProjectThrowsProIdExp(){
        //        given
        Project project = new Project();
        project.setId(1L);
        project.setProjectIdentifier("iden");
       given(projectRepository.findByProjectIdentifier(any())).willReturn(nullable(Project.class));

//        then
        assertThrows(ProjectIdException.class,()->projectService.deleteProject("iden"));
    }

    @Test
    void updateProject(){
//        Given
        BackLog backLog = new BackLog();
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setProjectName("Name");
        projectDTO.setProjectIdentifier("iden");

        Project savedProject = new Project();
        savedProject.setProjectName(projectDTO.getProjectName());
        savedProject.setId(1L);
        savedProject.setDescription(projectDTO.getDescription());
        savedProject.setProjectIdentifier(projectDTO.getProjectIdentifier());
        savedProject.setBackLog(backLog);


        backLog.setId(1L);
        backLog.setProject(savedProject);
        backLog.setProjectIdentifier(savedProject.getProjectIdentifier());

        given(projectRepository.save(any(Project.class))).willReturn(savedProject);
        given(backLogRepository.findByProjectIdentifier(anyString())).willReturn(backLog);

//        When
        ProjectDTO projectDTO1 = projectService.updateProject(1L,projectDTO);
//        Then
        assertEquals(projectDTO1.getProjectName(),savedProject.getProjectName());
        assertEquals(projectDTO1.getId(),savedProject.getId());
        assertEquals(projectDTO1.getStartDate(),savedProject.getStartDate());
        assertEquals(1L,projectDTO1.getBackLog().getId());
        assertEquals(savedProject.getProjectIdentifier(),projectDTO1.getBackLog().getProjectIdentifier());
        then(projectRepository).should().save(any(Project.class));
        then(projectRepository).shouldHaveNoMoreInteractions();
        then(backLogRepository).should().findByProjectIdentifier(anyString());
        then(projectRepository).shouldHaveNoMoreInteractions();
    }

}