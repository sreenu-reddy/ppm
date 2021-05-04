package com.sree.ppm.api.v1.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sree.ppm.api.v1.mapper.ProjectTaskMapper;
import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.api.v1.models.ProjectTaskListDTO;
import com.sree.ppm.domains.BackLog;
import com.sree.ppm.domains.Project;
import com.sree.ppm.domains.ProjectTask;
import com.sree.ppm.exceptions.ProjectIdException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BackLogControllerTest {

    public static final String PROJECT_NAME = "Name";
    public static final long ID = 3L;
    public static final String DESCRIPTION = "des";
    public static final String PROJECT_IDENTIFIER = "iden";
    public static final String SUMMARY = "summary";
    public static final long ID1 = 1L;
    public static final int PT_SEQUENCE = 0;
    public static final long ID2 = 2L;
    public static final String PROJECT_SEQUENCE = "seq-1";

    private final ProjectTaskMapper projectMapper = ProjectTaskMapper.INSTANCE;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock
    ProjectTaskService projectTaskService;
    @Mock
    BindingResult result;

    BackLogController backLogController;
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
        ProjectTaskDTo projectTaskDTo = getProjectTaskDTo(project);
        project.setBackLog(backLog);

        backLog.setId(ID);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        backLog.setProject(project);
        backLog.setProjectTasks(projectTasks);
        backLog.setPtSequence(PT_SEQUENCE);

        projectTasks.add(projectMapper.projectTaskDTOToProjectTask(projectTaskDTo));

        projectTaskDTo.setId(ID1);
        projectTaskDTo.setBackLog(backLog);
        projectTaskDTo.setSummary(SUMMARY);
        projectTaskDTo.setProjectSequence(backLog.getProjectIdentifier()+"-"+backLog.getPtSequence());
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
        ProjectTaskDTo projectTaskDTo = getdTo();

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
        ProjectTaskDTo projectTaskDTo = getProjectTaskDTo(project);
        project.setBackLog(backLog);

        backLog.setId(ID);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        backLog.setProject(project);
        backLog.setProjectTasks(projectTasks);
        backLog.setPtSequence(PT_SEQUENCE);

        projectTasks.add(projectMapper.projectTaskDTOToProjectTask(projectTaskDTo));

        projectTaskDTo.setId(ID1);
        projectTaskDTo.setBackLog(backLog);
        projectTaskDTo.setSummary("");
        projectTaskDTo.setProjectSequence(backLog.getProjectIdentifier()+"-"+backLog.getPtSequence());
        projectTaskDTo.setProjectIdentifier(project.getProjectIdentifier());

//        Then
        mockMvc.perform(post("/api/v1/backlog/iden")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(projectTaskDTo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.summary",equalTo("Please provide the summary of the projectTask")));

    }

    private ProjectTaskDTo getProjectTaskDTo(Project project) {
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        return projectTaskDTo;
    }

    @Test
    void getBackLogById(){
//        Given
        ProjectTaskListDTO listDTO = getProjectTaskListDTO();

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
        ProjectTaskListDTO listDTO = getProjectTaskListDTO();

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
        ProjectTaskDTo projectTaskDTo = getTaskDTo();

        given(projectTaskService.getProjectTaskByProjectSeq(anyString(),anyString())).willReturn(projectTaskDTo);

//        When
      var responseEntity=  backLogController.getProjectTaskByProjectSeq(PROJECT_IDENTIFIER, PROJECT_SEQUENCE);

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
        project.setId(ID1);
        project.setBackLog(backLog);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);

        backLog.setId(ID1);
        backLog.setProject(project);
        backLog.setProjectIdentifier(project.getProjectIdentifier());

        projectTaskDTo.setId(ID1);
        projectTaskDTo.setProjectSequence(PROJECT_SEQUENCE);
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
        project.setId(ID1);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);
        projectTaskDTo.setId(ID1);
        projectTaskDTo.setProjectSequence(PROJECT_SEQUENCE);
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
        project.setId(ID1);
        project.setBackLog(backLog);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);

        backLog.setId(ID1);
        backLog.setProject(project);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        given(projectTaskService.getProjectTaskByProjectSeq(anyString(),anyString())).willThrow(ProjectNotFoundException.class);

        //        Then
        mockMvc.perform(get("/api/v1/backlog/iden/seq")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProjectTask(){
//        Given
        ProjectTaskDTo updatedTask = getProjectTaskDTo();
        given(projectTaskService.updateProjectByProjectSeq(any(ProjectTaskDTo.class),anyString(),anyString())).willReturn(updatedTask);
//        When
      var response=  backLogController.updateProjectTask(updatedTask,PROJECT_IDENTIFIER,PROJECT_SEQUENCE);
//        Then
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void updateProjectTaskStatusIsOk() throws Exception {
//        Given
        ProjectTaskDTo updatedTask = getProjectTaskDTo(PROJECT_SEQUENCE);

        given(projectTaskService.updateProjectByProjectSeq(any(ProjectTaskDTo.class),anyString(),anyString())).willReturn(updatedTask);

//        then
        mockMvc.perform(put("/api/v1/backlog/iden/seq-1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary",equalTo("summary7899999999")));
    }

    @Test
    void updateProjectTaskStatusIs400() throws Exception {
//        given
        ProjectTaskDTo updatedTask = getProjectTaskDTo("seq-2");
        given(projectTaskService.updateProjectByProjectSeq(any(ProjectTaskDTo.class),anyString(),anyString())).willThrow(ProjectIdException.class);

//        then
        mockMvc.perform(put("/api/v1/backlog/iden/seq-1")
                .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(updatedTask)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deleteProjectSeq(){
//        Given
        extracted();

//        When
     var response=   backLogController.deleteProjectTask(PROJECT_IDENTIFIER,PROJECT_SEQUENCE);

//        Then
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void deleteProjectSeqStatusIsOk() throws Exception {
//        given
        extracted();
//        Then

        mockMvc.perform(delete("/api/v1/backlog/iden/seq-1")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    private ProjectTaskListDTO getProjectTaskListDTO() {
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        projectTaskDTo.setId(ID1);

        ProjectTaskDTo projectTaskDTo1 = new ProjectTaskDTo();
        projectTaskDTo1.setId(ID2);

        List<ProjectTaskDTo> projectTaskDTos = new ArrayList<>();
        projectTaskDTos.add(projectTaskDTo);
        projectTaskDTos.add(projectTaskDTo1);

        return new ProjectTaskListDTO(projectTaskDTos);
    }

    private ProjectTaskDTo getTaskDTo() {
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        Project project = new Project();
        BackLog backLog = new BackLog();
        project.setId(ID1);
        project.setBackLog(backLog);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);

        backLog.setId(ID1);
        backLog.setProject(project);
        backLog.setProjectIdentifier(project.getProjectIdentifier());

        projectTaskDTo.setId(ID1);
        projectTaskDTo.setProjectSequence(PROJECT_SEQUENCE);
        projectTaskDTo.setProjectIdentifier(backLog.getProjectIdentifier());
        projectTaskDTo.setBackLog(backLog);
        return projectTaskDTo;
    }


    private ProjectTaskDTo getProjectTaskDTo(String s) {
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        ProjectTaskDTo updatedTask = new ProjectTaskDTo();
        Project project = new Project();
        BackLog backLog = new BackLog();
        project.setId(ID1);
        project.setBackLog(backLog);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);

        backLog.setId(ID1);
        backLog.setProject(project);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        backLog.getProjectTasks().add(projectMapper.projectTaskDTOToProjectTask(projectTaskDTo));
        backLog.getProjectTasks().add(projectMapper.projectTaskDTOToProjectTask(updatedTask));

        projectTaskDTo.setId(ID1);
        projectTaskDTo.setProjectSequence(PROJECT_SEQUENCE);
        projectTaskDTo.setProjectIdentifier(backLog.getProjectIdentifier());
        projectTaskDTo.setBackLog(backLog);
        projectTaskDTo.setSummary("summary999999");

        updatedTask.setId(projectTaskDTo.getId());
        updatedTask.setBackLog(backLog);
        updatedTask.setProjectIdentifier(projectTaskDTo.getProjectIdentifier());
        updatedTask.setProjectSequence(s);
        updatedTask.setSummary("summary7899999999");
        return updatedTask;
    }

    private void extracted() {
        ProjectTaskDTo projectTaskDTo = new ProjectTaskDTo();
        ProjectTaskDTo updatedTask = new ProjectTaskDTo();
        Project project = new Project();
        BackLog backLog = new BackLog();
        project.setId(ID1);
        project.setBackLog(backLog);
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setProjectName(PROJECT_NAME);
        project.setDescription(DESCRIPTION);

        backLog.setId(ID1);
        backLog.setProject(project);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        backLog.getProjectTasks().add(projectMapper.projectTaskDTOToProjectTask(projectTaskDTo));
        backLog.getProjectTasks().add(projectMapper.projectTaskDTOToProjectTask(updatedTask));

        projectTaskDTo.setId(ID1);
        projectTaskDTo.setProjectSequence(PROJECT_SEQUENCE);
        projectTaskDTo.setProjectIdentifier(backLog.getProjectIdentifier());
        projectTaskDTo.setBackLog(backLog);
        projectTaskDTo.setSummary("summary999999");
    }

    private ProjectTaskDTo getProjectTaskDTo() {
        ProjectTaskDTo projectTaskDTo = getTaskDTo();
        projectTaskDTo.setSummary(SUMMARY);

        ProjectTaskDTo updatedTask = new ProjectTaskDTo();
        updatedTask.setId(projectTaskDTo.getId());
        updatedTask.setBackLog(projectTaskDTo.getBackLog());
        updatedTask.setProjectIdentifier(projectTaskDTo.getProjectIdentifier());
        updatedTask.setProjectSequence(PROJECT_SEQUENCE);
        updatedTask.setSummary(SUMMARY+1);
        return updatedTask;
    }

    private ProjectTaskDTo getdTo() {
        BackLog backLog = new BackLog();
        Project project = new Project();
        List<ProjectTask> projectTasks = new ArrayList<>();
        ProjectTaskDTo projectTaskDTo = getProjectTaskDTo(project);

        backLog.setId(ID);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        backLog.setProject(project);
        backLog.setProjectTasks(projectTasks);
        backLog.setPtSequence(PT_SEQUENCE);

        projectTasks.add(projectMapper.projectTaskDTOToProjectTask(projectTaskDTo));

        projectTaskDTo.setId(ID1);
        projectTaskDTo.setBackLog(backLog);
        projectTaskDTo.setSummary(SUMMARY);
        projectTaskDTo.setProjectSequence(backLog.getProjectIdentifier()+"-"+backLog.getPtSequence());
        projectTaskDTo.setProjectIdentifier(project.getProjectIdentifier());
        return projectTaskDTo;
    }
}