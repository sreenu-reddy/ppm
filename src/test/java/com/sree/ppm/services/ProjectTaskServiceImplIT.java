package com.sree.ppm.services;

import com.sree.ppm.BootStrapData;
import com.sree.ppm.api.v1.controller.BackLogController;
import com.sree.ppm.api.v1.mapper.ProjectTaskMapper;
import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.domains.BackLog;
import com.sree.ppm.domains.Project;
import com.sree.ppm.domains.ProjectTask;
import com.sree.ppm.repositories.BackLogRepository;
import com.sree.ppm.repositories.ProjectRepository;
import com.sree.ppm.repositories.ProjectTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProjectTaskServiceImplIT {
    @Autowired
   ProjectTaskRepository projectTaskRepository;
    @Autowired
    BackLogRepository backLogRepository;

    @Autowired
    ProjectRepository projectRepository;

    ProjectTaskService projectTaskService;

   private final ProjectTaskMapper projectTaskMapper = ProjectTaskMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        projectTaskService = new ProjectTaskServiceImpl(projectTaskRepository,backLogRepository);

        BootStrapData bootStrapData=new BootStrapData(projectRepository,backLogRepository,projectTaskRepository);
        bootStrapData.run();
    }

    @Test
    void createProjectTask() {
        //        Given
        BackLog backLog = backLogRepository.findByProjectIdentifier("first");
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        projectTaskDTo.setProjectIdentifier(backLog.getProjectIdentifier());
        projectTaskDTo.setSummary("summary");
        projectTaskDTo.setBackLog(backLog);
        backLog.getProjectTasks().add(projectTaskMapper.projectTaskDTOToProjectTask(projectTaskDTo));



//        when
        ProjectTaskDTo projectTaskDTo1 = projectTaskService.createProjectTask(projectTaskDTo.getProjectIdentifier(),projectTaskDTo);

//        Then
        assertNotNull(projectTaskDTo1);
        assertEquals(0,projectTaskDTo1.getPriority());
        assertEquals("To-Do",projectTaskDTo1.getStatus());
        assertEquals(backLog.getProject(),projectTaskDTo1.getBackLog().getProject());
        assertEquals(backLog,projectTaskDTo1.getBackLog());
    }
}