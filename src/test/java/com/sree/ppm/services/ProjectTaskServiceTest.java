package com.sree.ppm.services;

import com.sree.ppm.api.v1.mapper.ProjectTaskMapper;
import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.api.v1.models.ProjectTaskListDTO;
import com.sree.ppm.domains.BackLog;
import com.sree.ppm.domains.Project;
import com.sree.ppm.domains.ProjectTask;
import com.sree.ppm.exceptions.ProjectNotFoundException;
import com.sree.ppm.repositories.BackLogRepository;
import com.sree.ppm.repositories.ProjectRepository;
import com.sree.ppm.repositories.ProjectTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    public static final String SUMMARY_1 = "Summary_1";
    public static final String PROJECT_SEQUENCE = "seq-1";
    ProjectTaskService projectTaskService;

    @Mock
    ProjectTaskRepository projectTaskRepository;

    @Mock
    BackLogRepository backLogRepository;

    @Mock
    ProjectRepository projectRepository;

    @Captor
    ArgumentCaptor<String> Identifier;

    private final ProjectTaskMapper mapper = ProjectTaskMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        projectTaskService = new ProjectTaskServiceImpl(projectTaskRepository,backLogRepository, projectRepository);
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
        backLog.setPtSequence(2);
        backLog.setProjectTasks(projectTasks);

        ProjectTask projectTask = new ProjectTask();
        projectTask.setId(ID);
        projectTask.setProjectIdentifier(backLog.getProjectIdentifier());
        projectTask.setBackLog(backLog);
        projectTask.setSummary(SUMMARY);
        projectTasks.add(projectTask);
        projectTask.setProjectSequence(project.getProjectIdentifier()+"-"+backLog.getPtSequence());
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
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
//        When
        assertThrows(ProjectNotFoundException.class,()->projectTaskService.createProjectTask(null,projectTaskDTo));
    }

    @Test
    void getAllProjectsTasks(){
//        Given
        Project project = new Project();
        BackLog backLog = new BackLog();
        project.setId(1L);
        project.setBackLog(backLog);

        List<ProjectTask> projectTasks = new ArrayList<>();
        backLog.setId(1L);
        backLog.setProjectIdentifier(PROJECT_IDENTIFIER);
        ProjectTask projectTask = new ProjectTask();
        projectTask.setId(1L);
        projectTask.setProjectIdentifier(PROJECT_IDENTIFIER);
        projectTask.setSummary(SUMMARY_1);
        projectTask.setBackLog(backLog);

        ProjectTask projectTask1 = new ProjectTask();
        projectTask1.setId(2L);
        projectTask1.setProjectIdentifier(PROJECT_IDENTIFIER);
        projectTask1.setBackLog(backLog);
        projectTask1.setSummary("Summary_2");
        projectTasks.add(projectTask);
        projectTasks.add(projectTask1);
        given(projectRepository.findByProjectIdentifier(Identifier.capture())).willReturn(project);
        given(projectTaskRepository.findByProjectIdentifierOrderByPriority(Identifier.capture())).willReturn(projectTasks);
//        When
        ProjectTaskListDTO projectTaskDTOs = projectTaskService.getAllProjectTasks(PROJECT_IDENTIFIER);

//        Then
        assertNotNull(projectTaskDTOs.getProjectTaskDTos());
        assertEquals(2,projectTaskDTOs.getProjectTaskDTos().size());
        assertTrue(projectTaskDTOs.getProjectTaskDTos().contains(mapper.projectTaskToProjectTaskDTO(projectTask)));
        assertEquals(PROJECT_IDENTIFIER,Identifier.getValue());
        then(projectTaskRepository).should().findByProjectIdentifierOrderByPriority(Identifier.getValue());
        then(projectTaskRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void getAllProjectsTasksThrowsProjectNotFoundExp(){
//        Given
        given(projectRepository.findByProjectIdentifier(anyString())).willReturn(null);

//        Then
        assertThrows(ProjectNotFoundException.class,()->projectTaskService.getAllProjectTasks(PROJECT_IDENTIFIER));
    }

    @Test
    void getProjectTaskByProjectSeq(){
//        Given
        Project project = new Project();
        BackLog backLog = new BackLog();
        project.setId(1L);
        project.setBackLog(backLog);

        backLog.setId(1L);
        backLog.setProjectIdentifier(PROJECT_IDENTIFIER);
        backLog.setProject(project);
        ProjectTask projectTask = new ProjectTask();
        projectTask.setId(1L);
        projectTask.setProjectIdentifier(PROJECT_IDENTIFIER);
        projectTask.setSummary(SUMMARY_1);
        projectTask.setBackLog(backLog);
        projectTask.setProjectSequence(PROJECT_SEQUENCE);

        given(backLogRepository.findByProjectIdentifier(anyString())).willReturn(backLog);
        given(projectTaskRepository.findByProjectSequence(anyString())).willReturn(projectTask);
//        When
       var pt= projectTaskService.getProjectTaskByProjectSeq(PROJECT_IDENTIFIER, PROJECT_SEQUENCE);

//        Then
        assertNotNull(pt);
        assertEquals(PROJECT_IDENTIFIER,pt.getProjectIdentifier());
        assertEquals(SUMMARY_1,pt.getSummary());
        assertEquals(PROJECT_SEQUENCE,pt.getProjectSequence());
        assertNull(pt.getPriority());
        assertNull(pt.getDueDate());
        assertNull(pt.getAcceptanceCriteria());
        assertEquals(backLog.getId(),pt.getBackLog().getId());
        then(backLogRepository).should().findByProjectIdentifier(anyString());
        then(backLogRepository).shouldHaveNoMoreInteractions();
        then(projectTaskRepository).should().findByProjectSequence(anyString());
        then(projectTaskRepository).shouldHaveNoMoreInteractions();

    }

    @Test
    void getProjectTaskByProjectSeqWithNullBackLogExp(){
//        given
        given(backLogRepository.findByProjectIdentifier(anyString())).willReturn(null);

//        then
        assertThrows(ProjectNotFoundException.class,()->projectTaskService.getProjectTaskByProjectSeq(PROJECT_IDENTIFIER,PROJECT_SEQUENCE));
    }

    @Test
    void getProjectTaskByProjectSeqWithNullProjectTaskExp(){
////        given
        Project project = new Project();
        BackLog backLog = new BackLog();
        project.setId(1L);
        project.setBackLog(backLog);

        backLog.setId(1L);
        backLog.setProjectIdentifier(PROJECT_IDENTIFIER);
        backLog.setProject(project);
        given(backLogRepository.findByProjectIdentifier(anyString())).willReturn(backLog);
        given(projectTaskRepository.findByProjectSequence(anyString())).willReturn(null);

//        then
        assertThrows(ProjectNotFoundException.class,()->projectTaskService.getProjectTaskByProjectSeq(PROJECT_IDENTIFIER,PROJECT_SEQUENCE));
    }


    @Test
    void UpdateProjectTask(){
//        Given
        ProjectTask projectTask = new ProjectTask();
        ProjectTask updatedTask = new ProjectTask();
        BackLog backLog = new BackLog();
        Project project = new Project();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
       project.setBackLog(backLog);

        backLog.setId(ID);
        backLog.setProject(project);
        backLog.setProjectIdentifier(PROJECT_IDENTIFIER);

        projectTask.setId(ID);
        projectTask.setProjectIdentifier(PROJECT_IDENTIFIER);
        projectTask.setSummary(SUMMARY);
        projectTask.setBackLog(backLog);
        backLog.getProjectTasks().add(projectTask);
        projectTask.setProjectSequence(PROJECT_SEQUENCE);

        updatedTask.setId(projectTask.getId());
        updatedTask.setProjectIdentifier(projectTask.getProjectIdentifier());
        updatedTask.setSummary(SUMMARY_1);
        updatedTask.setBackLog(projectTask.getBackLog());
        backLog.getProjectTasks().remove(projectTask);
        backLog.getProjectTasks().add(updatedTask);
        updatedTask.setProjectSequence(PROJECT_SEQUENCE);
        given(backLogRepository.findByProjectIdentifier(PROJECT_IDENTIFIER)).willReturn(backLog);
        given(projectTaskRepository.findByProjectSequence(anyString())).willReturn(projectTask);
        given(projectTaskRepository.save(any(ProjectTask.class))).willReturn(updatedTask);

//        When
      var projectTaskDTo=  projectTaskService.updateProjectByProjectSeq(mapper.projectTaskToProjectTaskDTO(updatedTask),PROJECT_IDENTIFIER,PROJECT_SEQUENCE);
//      Then
        assertNotNull(projectTaskDTo);
        assertEquals(ID,projectTaskDTo.getId());
        assertEquals(projectTask.getBackLog(),projectTaskDTo.getBackLog());
        assertEquals(SUMMARY_1,projectTaskDTo.getSummary());
        InOrder inOrder = Mockito.inOrder(backLogRepository,projectTaskRepository,projectTaskRepository);
        inOrder.verify(backLogRepository).findByProjectIdentifier(anyString());
        inOrder.verify(projectTaskRepository).findByProjectSequence(anyString());
        inOrder.verify(projectTaskRepository).save(any(ProjectTask.class));
    }

    @Test
    void deleteProjectTask(){
//        Given
        ProjectTask projectTask = new ProjectTask();
        ProjectTask updatedTask = new ProjectTask();
        BackLog backLog = new BackLog();
        Project project = new Project();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setBackLog(backLog);

        backLog.setId(ID);
        backLog.setProject(project);
        backLog.setProjectIdentifier(PROJECT_IDENTIFIER);

        projectTask.setId(ID);
        projectTask.setProjectIdentifier(PROJECT_IDENTIFIER);
        projectTask.setSummary(SUMMARY);
        projectTask.setBackLog(backLog);
        backLog.getProjectTasks().add(projectTask);
        projectTask.setProjectSequence(PROJECT_SEQUENCE);

        updatedTask.setId(projectTask.getId());
        updatedTask.setProjectIdentifier(projectTask.getProjectIdentifier());
        updatedTask.setSummary(SUMMARY_1);
        updatedTask.setBackLog(projectTask.getBackLog());
        backLog.getProjectTasks().remove(projectTask);
        backLog.getProjectTasks().add(updatedTask);
        updatedTask.setProjectSequence(PROJECT_SEQUENCE);
        given(backLogRepository.findByProjectIdentifier(PROJECT_IDENTIFIER)).willReturn(backLog);
        given(projectTaskRepository.findByProjectSequence(anyString())).willReturn(projectTask);
//        When

        projectTaskService.deleteProjectSeq(PROJECT_IDENTIFIER,PROJECT_SEQUENCE);
//        Then
        InOrder inOrder = Mockito.inOrder(backLogRepository,projectTaskRepository,projectTaskRepository);
        inOrder.verify(backLogRepository).findByProjectIdentifier(anyString());
        inOrder.verify(projectTaskRepository).findByProjectSequence(anyString());
        inOrder.verify(projectTaskRepository).delete(any(ProjectTask.class));
    }
}