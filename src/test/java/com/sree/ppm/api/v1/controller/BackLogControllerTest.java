package com.sree.ppm.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sree.ppm.api.v1.mapper.ProjectTaskMapper;
import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.api.v1.models.ProjectTaskListDTO;
import com.sree.ppm.domains.BackLog;
import com.sree.ppm.domains.Project;
import com.sree.ppm.domains.ProjectTask;
import com.sree.ppm.exceptions.ProjectNotFoundException;
import com.sree.ppm.services.ProjectTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BackLogControllerTest {

    public static final String PROJECT_NAME = "Name";
    public static final long ID = 3L;
    public static final String DESCRIPTION = "des";
    public static final String PROJECT_IDENTIFIER = "iden";
    public static final String SUMMARY = "summary";
    @Mock
    ProjectTaskService projectTaskService;

    BackLogController backLogController;


    private final ProjectTaskMapper projectMapper = ProjectTaskMapper.INSTANCE;
    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    BindingResult result;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        backLogController = new BackLogController(projectTaskService);
        mockMvc = MockMvcBuilders.standaloneSetup(backLogController).build();

    }

    @Test
    void createProjectTask() {
//        Given
        BackLog backLog = new BackLog();
        Project project = new Project();
        List<ProjectTask> projectTasks = new ArrayList<>();
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setBackLog(backLog);

        backLog.setId(ID);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        backLog.setProject(project);
        backLog.setProjectTasks(projectTasks);
        backLog.setPTSequence(0);

        projectTasks.add(projectMapper.projectTaskDTOToProjectTask(projectTaskDTo));

        projectTaskDTo.setId(1L);
        projectTaskDTo.setBackLog(backLog);
        projectTaskDTo.setSummary(SUMMARY);
        projectTaskDTo.setProjectSequence(backLog.getProjectIdentifier()+"-"+backLog.getPTSequence());
        projectTaskDTo.setProjectIdentifier(project.getProjectIdentifier());

//        When
      var  responseEntity=  backLogController.createProjectTask(projectTaskDTo,result,project.getProjectIdentifier());

//        Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
    }

    @Test
    void createProjectTaskStatus() throws Exception {
        //        Given
        BackLog backLog = new BackLog();
        Project project = new Project();
        List<ProjectTask> projectTasks = new ArrayList<>();
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);

        backLog.setId(ID);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        backLog.setProject(project);
        backLog.setProjectTasks(projectTasks);
        backLog.setPTSequence(0);

        projectTasks.add(projectMapper.projectTaskDTOToProjectTask(projectTaskDTo));

        projectTaskDTo.setId(1L);
        projectTaskDTo.setBackLog(backLog);
        projectTaskDTo.setSummary(SUMMARY);
        projectTaskDTo.setProjectSequence(backLog.getProjectIdentifier()+"-"+backLog.getPTSequence());
        projectTaskDTo.setProjectIdentifier(project.getProjectIdentifier());

//        Then
        mockMvc.perform(post("/api/v1/backlog/iden")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(projectTaskDTo)))
                .andExpect(status().isCreated());
    }

    @Test
    void createProjectTaskStatus404() throws Exception {
        //        Given
        BackLog backLog = new BackLog();
        Project project = new Project();
        List<ProjectTask> projectTasks = new ArrayList<>();
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setBackLog(backLog);

        backLog.setId(ID);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        backLog.setProject(project);
        backLog.setProjectTasks(projectTasks);
        backLog.setPTSequence(0);

        projectTasks.add(projectMapper.projectTaskDTOToProjectTask(projectTaskDTo));

        projectTaskDTo.setId(1L);
        projectTaskDTo.setBackLog(backLog);
        projectTaskDTo.setSummary("");
        projectTaskDTo.setProjectSequence(backLog.getProjectIdentifier()+"-"+backLog.getPTSequence());
        projectTaskDTo.setProjectIdentifier(project.getProjectIdentifier());

//        Then
        mockMvc.perform(post("/api/v1/backlog/iden")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(projectTaskDTo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.summary",equalTo("Please provide the summary of the projectTask")));

    }

    @Test
    void getBackLogById(){
//        Given
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        projectTaskDTo.setId(1L);

        ProjectTaskDTo projectTaskDTo1 = new ProjectTaskDTo();
        projectTaskDTo1.setId(2L);

        List<ProjectTaskDTo> projectTaskDTos = new ArrayList<>();
        projectTaskDTos.add(projectTaskDTo);
        projectTaskDTos.add(projectTaskDTo1);

        ProjectTaskListDTO listDTO = new ProjectTaskListDTO(projectTaskDTos);

          given(projectTaskService.getAllProjectTasks(anyString())).willReturn(listDTO);
//        When
         var result = backLogController.getBackLogById(PROJECT_IDENTIFIER);
//        Then
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK,result.getStatusCode());
    }


    @Test
    void getBackLogStatusOk() throws Exception {
//        Given
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        projectTaskDTo.setId(1L);

        ProjectTaskDTo projectTaskDTo1 = new ProjectTaskDTo();
        projectTaskDTo1.setId(2L);

        List<ProjectTaskDTo> projectTaskDTos = new ArrayList<>();
        projectTaskDTos.add(projectTaskDTo);
        projectTaskDTos.add(projectTaskDTo1);

        ProjectTaskListDTO listDTO = new ProjectTaskListDTO(projectTaskDTos);

        given(projectTaskService.getAllProjectTasks(anyString())).willReturn(listDTO);

//        When
        mockMvc.perform(get("/api/v1/backlog/iden")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBackLogStatus400() throws Exception {
//        Given
        given(projectTaskService.getAllProjectTasks(anyString())).willThrow(ProjectNotFoundException.class);

//        then
        mockMvc.perform(get("/api/v1/backlog/idenhhhhh")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProjectTaskByProjectSeq(){
//        Given
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        Project project = new Project();
        BackLog backLog = new BackLog();
        project.setId(1L);
        project.setBackLog(backLog);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);

        backLog.setId(1L);
        backLog.setProject(project);
        backLog.setProjectIdentifier(project.getProjectIdentifier());

        projectTaskDTo.setId(1L);
        projectTaskDTo.setProjectSequence("seq-1");
        projectTaskDTo.setProjectIdentifier(backLog.getProjectIdentifier());
        projectTaskDTo.setBackLog(backLog);

        given(projectTaskService.getProjectTaskByProjectSeq(anyString(),anyString())).willReturn(projectTaskDTo);

//        When
      var responseEntity=  backLogController.getProjectTaskByProjectSeq(PROJECT_IDENTIFIER,"seq-1");

//      then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }

    @Test
    void getProjectTaskByProjectSeqStatusOk() throws Exception {
//        given
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        Project project = new Project();
        BackLog backLog = new BackLog();
        project.setId(1L);
        project.setBackLog(backLog);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);

        backLog.setId(1L);
        backLog.setProject(project);
        backLog.setProjectIdentifier(project.getProjectIdentifier());

        projectTaskDTo.setId(1L);
        projectTaskDTo.setProjectSequence("seq-1");
        projectTaskDTo.setProjectIdentifier(backLog.getProjectIdentifier());
        projectTaskDTo.setBackLog(backLog);

        given(projectTaskService.getProjectTaskByProjectSeq(anyString(),anyString())).willReturn(projectTaskDTo);

//        Then
        mockMvc.perform(get("/api/v1/backlog/iden/seq-1")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getProjectTaskByProjectSeqStatus400WhenBackLogIsNull() throws Exception {
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        Project project = new Project();
        project.setId(1L);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);
        projectTaskDTo.setId(1L);
        projectTaskDTo.setProjectSequence("seq-1");
        given(projectTaskService.getProjectTaskByProjectSeq(anyString(),anyString())).willThrow(ProjectNotFoundException.class);

        //        Then
        mockMvc.perform(get("/api/v1/backlog/idenh/seq-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProjectTaskByProjectSeqStatus400WhenPTIsNull() throws Exception {
        Project project = new Project();
        BackLog backLog = new BackLog();
        project.setId(1L);
        project.setBackLog(backLog);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);

        backLog.setId(1L);
        backLog.setProject(project);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        given(projectTaskService.getProjectTaskByProjectSeq(anyString(),anyString())).willThrow(ProjectNotFoundException.class);

        //        Then
        mockMvc.perform(get("/api/v1/backlog/iden/seq")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}