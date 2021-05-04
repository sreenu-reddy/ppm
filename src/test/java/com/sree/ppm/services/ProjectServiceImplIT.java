package com.sree.ppm.services;

import com.sree.ppm.bootstrap.BootStrapData;
import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.exceptions.ProjectIdException;
import com.sree.ppm.repositories.BackLogRepository;
import com.sree.ppm.repositories.ProjectRepository;
import com.sree.ppm.repositories.ProjectTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest

class ProjectServiceImplIT {

    public static final String PROJECT_IDENTIFIER = "Hello";
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    BackLogRepository backLogRepository;

    @Autowired
    ProjectTaskRepository projectTaskRepository;


    @BeforeEach
    void setUp() {
        projectService = new ProjectServiceImpl(projectRepository,backLogRepository);
        BootStrapData bootStrapData=new BootStrapData(projectRepository,backLogRepository,projectTaskRepository);
        bootStrapData.run();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void updateProjectWillThrowsProjectIDExp() {
//        Given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectIdentifier(PROJECT_IDENTIFIER);
//        Then
        assertThrows(ProjectIdException.class,()->projectService.updateProject(1L,projectDTO));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void createNewProject(){
//        Given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectIdentifier("11e1");
        projectDTO.setProjectName("new");
        projectDTO.setDescription("Des");

//        When
      var response=  projectService.createNewProject(projectDTO);

//        Then
        assertNotNull(response);
        assertEquals("11E1",response.getProjectIdentifier());
        assertEquals("new",response.getProjectName());
        assertEquals("Des",response.getDescription());
        assertNull(response.getStartDate());
        assertNull(response.getEndDate());
        assertNotNull(response.getBackLog());
        assertEquals("11E1",response.getBackLog().getProjectIdentifier());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void createNewProjectThrowsProjectIdExp(){
//        Given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectIdentifier("first");
        projectDTO.setProjectName("new");
        projectDTO.setDescription("Des");
//        Then
        assertThrows(ProjectIdException.class,()->projectService.createNewProject(projectDTO));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getProjectById(){

//        When
        var response = projectService.getProjectByIdentifier("first");
//        Then
        assertNotNull(response);
    }
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getAllProjects(){

//        When
        var response = projectService.getAllProjects();
//        Then
        assertNotNull(response.getProjects());
        assertEquals(2,response.getProjects().size());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void updateProject(){
//        given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectIdentifier("first");
        projectDTO.setProjectName("new");
        projectDTO.setDescription("Des");
//        when
        var response= projectService.updateProject(1L,projectDTO);

//        Then
        assertNotNull(response);
        assertEquals("FIRST",response.getProjectIdentifier());
        assertEquals("new",response.getProjectName());
        assertEquals("Des",response.getDescription());
        assertNull(response.getStartDate());
        assertNull(response.getEndDate());
        assertNotNull(response.getBackLog());
        assertEquals("FIRST",response.getBackLog().getProjectIdentifier());

    }

}