package com.sree.ppm.services;

import com.sree.ppm.bootstrap.BootStrapData;

import com.sree.ppm.api.v1.mapper.ProjectTaskMapper;
import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.domains.BackLog;
import com.sree.ppm.exceptions.ProjectIdException;
import com.sree.ppm.exceptions.ProjectNotFoundException;
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
        projectTaskService = new ProjectTaskServiceImpl(projectTaskRepository,backLogRepository, projectRepository);

        BootStrapData bootStrapData=new BootStrapData(projectRepository,backLogRepository,projectTaskRepository);
        bootStrapData.run();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void createProjectTask() {
        //        Given
        BackLog backLog = backLogRepository.findByProjectIdentifier("FIRST");
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

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void createProjectTaskWithEmptyStatus() {
        //        Given
        BackLog backLog = backLogRepository.findByProjectIdentifier("FIRST");
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        projectTaskDTo.setProjectIdentifier(backLog.getProjectIdentifier());
        projectTaskDTo.setSummary("summary");
        projectTaskDTo.setStatus("");
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

    @Test
    void getProjectTaskBYProjectSeq(){
//        When
       var response= projectTaskService.getProjectTaskByProjectSeq("SEE1","SEE1-0");
//       Then
       assertNotNull(response);
       assertEquals("SEE1",response.getProjectIdentifier());
       assertEquals("SEE1-0",response.getProjectSequence());
       assertNull(response.getDueDate());
       assertNull(response.getAcceptanceCriteria());
       assertNull(response.getPriority());
       assertNotNull(response.getBackLog().getId());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
   void getProjectTaskByProjectSeqWillThrowsExp(){

        assertThrows(ProjectNotFoundException.class,()->projectTaskService.getProjectTaskByProjectSeq("FIRST","SEE1-0"));
   }

   @Test
   @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
   void updateProjectTask(){
//        Given

      var response = projectTaskService.getProjectTaskByProjectSeq("SEE1","SEE1-0");

       response.setSummary("Updated Summary");
       response.setPriority(2);
       response.setStatus("progress");
       response.setAcceptanceCriteria("testsShouldPass");
//       When
       var response1 = projectTaskService.updateProjectByProjectSeq(response,"SEE1","SEE1-0");

//       Then
       assertNotNull(response1);
       assertEquals(2,response1.getPriority());
       assertEquals("progress",response1.getStatus());
       assertNull(response1.getDueDate());
       assertEquals("testsShouldPass",response1.getAcceptanceCriteria());
       assertEquals("SEE1",response1.getProjectIdentifier());

   }



   @Test
   @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
   void UpdateProjectTaskWillThrowsProjectIdExpWhenProjectIdentifierUpdate(){
//        Given
       ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
       projectTaskDTo.setProjectIdentifier("hello");

//       Then
       assertThrows(ProjectIdException.class,()->projectTaskService.updateProjectByProjectSeq(projectTaskDTo,"SEE1","SEE1-0"));

   }

   @Test
   @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
   void UpdateProjectTaskWillThrowsProjectIdExpWhenProjectSequenceUpdate(){
//        Given
       ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
       projectTaskDTo.setProjectIdentifier("SEE1");
       projectTaskDTo.setProjectSequence("SEE1-11");

//       Then
       assertThrows(ProjectIdException.class,()->projectTaskService.updateProjectByProjectSeq(projectTaskDTo,"SEE1","SEE1-0"));

   }

   @Test
   @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
   void deleteProjectTask(){
//        WHen
        projectTaskService.deleteProjectSeq("SEE1","SEE1-0");

//        Then
       assertNull(projectTaskRepository.findByProjectSequence("SEE1-0"));
   }


}