package com.sree.ppm.services;

import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.domains.BackLog;
import com.sree.ppm.domains.Project;
import com.sree.ppm.domains.ProjectTask;
import com.sree.ppm.exceptions.ProjectNotFoundException;
import com.sree.ppm.repositories.BackLogRepository;
import com.sree.ppm.repositories.ProjectTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProjectTaskServiceTest {

    public static final String PROJECT_IDENTIFIER = "iden";
    public static final String PROJECT_NAME = "project";
    public static final String DESCRIPTION = "desc";
    public static final long ID = 1L;
    public static final String SUMMARY = "summary";
    public static final int PRIORITY = 5;
    public static final String STATUS = "done";
    ProjectTaskService projectTaskService;

    @Mock
    ProjectTaskRepository projectTaskRepository;

    @Mock
    BackLogRepository backLogRepository;

    @BeforeEach
    void setUp() {
        projectTaskService = new ProjectTaskServiceImpl(projectTaskRepository,backLogRepository);
    }

    @Test
    void createProjectTask() {
//        Given
        Project project = new Project();
        BackLog backLog = new BackLog();
        List<ProjectTask> projectTasks = new ArrayList<>();
        project.setId(ID);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);

        backLog.setId(ID);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        backLog.setProject(project);
        backLog.setPTSequence(2);
        backLog.setProjectTasks(projectTasks);

        ProjectTask projectTask = new ProjectTask();
        projectTask.setId(ID);
        projectTask.setProjectIdentifier(backLog.getProjectIdentifier());
        projectTask.setBackLog(backLog);
        projectTask.setSummary(SUMMARY);
        projectTasks.add(projectTask);
        projectTask.setProjectSequence(project.getProjectIdentifier()+"-"+backLog.getPTSequence());
        projectTask.setPriority(PRIORITY);
        projectTask.setStatus(STATUS);

        projectTasks.add(projectTask);

        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        projectTaskDTo.setId(projectTask.getId());
        projectTaskDTo.setPriority(projectTask.getPriority());
        projectTaskDTo.setProjectSequence(projectTask.getProjectSequence());
        projectTaskDTo.setProjectIdentifier(projectTask.getProjectIdentifier());
        projectTaskDTo.setSummary(projectTask.getSummary());
        projectTaskDTo.setBackLog(projectTask.getBackLog());
        projectTaskDTo.setProjectSequence(projectTask.getProjectSequence());
        projectTaskDTo.setProjectIdentifier(projectTask.getProjectIdentifier());
        projectTaskDTo.setStatus(projectTask.getStatus());
        given(backLogRepository.findByProjectIdentifier(anyString())).willReturn(backLog);
        given(projectTaskRepository.save(any(ProjectTask.class))).willReturn(projectTask);

//        when
        ProjectTaskDTo projectTaskDTo1 = projectTaskService.createProjectTask(projectTask.getProjectIdentifier(),projectTaskDTo);

//        Then
        assertNotNull(projectTaskDTo1);
        assertEquals("iden-2",projectTaskDTo1.getProjectSequence());
        assertEquals(PRIORITY,projectTaskDTo1.getPriority());
        assertEquals(STATUS,projectTaskDTo1.getStatus());
        assertEquals(project.getProjectIdentifier(),projectTaskDTo1.getProjectIdentifier());
        assertEquals(backLog,projectTaskDTo1.getBackLog());
        InOrder inOrder = Mockito.inOrder(backLogRepository,projectTaskRepository);
        inOrder.verify(backLogRepository).findByProjectIdentifier(anyString());
        inOrder.verify(projectTaskRepository).save(any(ProjectTask.class));
        then(backLogRepository).should().findByProjectIdentifier(anyString());
        then(backLogRepository).shouldHaveNoMoreInteractions();
        then(projectTaskRepository).should().save(any(ProjectTask.class));
        then(projectTaskRepository).shouldHaveNoMoreInteractions();

    }

    @Test
    void createProjectTaskThrowsExp(){
//        Given
        given(backLogRepository.findByProjectIdentifier(anyString())).willThrow(NullPointerException.class);
//        When
        assertThrows(ProjectNotFoundException.class,()->projectTaskService.createProjectTask(null,new ProjectTaskDTo()));
    }


}