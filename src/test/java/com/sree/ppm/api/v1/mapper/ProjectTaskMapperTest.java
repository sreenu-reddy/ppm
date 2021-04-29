package com.sree.ppm.api.v1.mapper;

import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.domains.ProjectTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTaskMapperTest {

    public static final long ID = 1L;
    public static final String PROJECT_IDENTIFIER = "iden";
    public static final int PRIORITY = 2;
    public static final String SUMMARY = "task";
    ProjectTaskMapper projectTaskMapper = ProjectTaskMapper.INSTANCE;

    @Test
    void projectTaskToProjectTaskDTOReturnsNull() {
        assertNull(projectTaskMapper.projectTaskToProjectTaskDTO(null));
    }

    @Test
    void projectTaskToProjectTaskDTOReturnsEmptyObj(){
        assertNotNull(projectTaskMapper.projectTaskToProjectTaskDTO(new ProjectTask()));
    }

    @Test
    void projectTaskToProjectTaskDTO(){
//        Given
        ProjectTask projectTask = new ProjectTask();
        projectTask.setId(ID);
        projectTask.setProjectIdentifier(PROJECT_IDENTIFIER);
        projectTask.setPriority(PRIORITY);
        projectTask.setSummary(SUMMARY);

//        When
        ProjectTaskDTo projectTaskDTo = projectTaskMapper.projectTaskToProjectTaskDTO(projectTask);
//        Then
        assertNotNull(projectTaskDTo);
        assertEquals(projectTask.getId(),projectTaskDTo.getId());
        assertEquals(projectTask.getSummary(),projectTaskDTo.getSummary());
        assertEquals(PRIORITY,projectTaskDTo.getPriority());
        assertNull(projectTaskDTo.getProjectSequence());
        assertNull(projectTaskDTo.getAcceptanceCriteria());
        assertNull(projectTaskDTo.getBackLog());
        assertNull(projectTaskDTo.getStatus());
    }


    @Test
    void projectTaskDTOToProjectTaskReturnsNull() {
        assertNull(projectTaskMapper.projectTaskDTOToProjectTask(null));
    }

    @Test
    void projectTaskDTOToProjectTaskReturnsEmptyObj(){
         assertNotNull(projectTaskMapper.projectTaskDTOToProjectTask(new ProjectTaskDTo()));
    }

    @Test
    void projectTaskDTOToProjectTask(){
//        given
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        projectTaskDTo.setId(ID);
        projectTaskDTo.setProjectIdentifier(PROJECT_IDENTIFIER);
        projectTaskDTo.setSummary(SUMMARY);
        projectTaskDTo.setPriority(PRIORITY);
//        When
        ProjectTask projectTask = projectTaskMapper.projectTaskDTOToProjectTask(projectTaskDTo);
//        Then
        assertNotNull(projectTask);
        assertEquals(ID,projectTask.getId());
        assertEquals(PROJECT_IDENTIFIER,projectTask.getProjectIdentifier());
        assertEquals(PRIORITY,projectTask.getPriority());
        assertEquals(SUMMARY,projectTask.getSummary());
        assertNull(projectTask.getProjectSequence());
        assertNull(projectTask.getAcceptanceCriteria());
        assertNull(projectTask.getBackLog());
        assertNull(projectTask.getStatus());
    }
}