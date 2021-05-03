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
}