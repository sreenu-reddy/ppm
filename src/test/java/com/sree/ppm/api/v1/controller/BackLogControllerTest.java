package com.sree.ppm.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sree.ppm.api.v1.mapper.ProjectTaskMapper;
import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.domains.BackLog;
import com.sree.ppm.domains.Project;
import com.sree.ppm.domains.ProjectTask;
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
}