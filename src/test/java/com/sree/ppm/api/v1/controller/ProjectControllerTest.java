package com.sree.ppm.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.api.v1.models.ProjectListDTO;
import com.sree.ppm.domains.Project;
import com.sree.ppm.exceptions.ProjectIdException;
import com.sree.ppm.exceptions.ProjectNotFoundException;
import com.sree.ppm.services.ProjectService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {
    public static final long ID = 1L;
    public static final String PROJECT_NAME = "ProjectName";
    public static final String PROJECT_IDENTIFIER = "Identifier";
    public static final String DESCRIPTION = "Description";
    public static final String PROJECT_IDENTIFIER1 = "Iden";
    public static final int EXPECTED_SIZE = 1;
    ProjectController controller;
    @Mock
    ProjectService projectService;
    @Mock
    BindingResult bindingResult;

    MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        controller = new ProjectController(projectService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createNewProject() throws NoSuchMethodException {
//        given
        ProjectDTO project = getProjectDTO(PROJECT_IDENTIFIER);
        given(projectService.createNewProject(any(ProjectDTO.class))).willReturn(project);

//        When

        ResponseEntity<Object> project1 = controller.createNewProject(project,bindingResult);
        System.out.println(project1);

//        then
        assertNotNull(project1.getBody());

        assertEquals(HttpStatus.CREATED,project1.getStatusCode());
        assertEquals(project.getClass().getDeclaredMethod("getProjectIdentifier"), Objects.requireNonNull(project1.getBody()).getClass().getDeclaredMethod("getProjectIdentifier"));
        then(projectService).should().createNewProject(any(ProjectDTO.class));
        then(projectService).shouldHaveNoMoreInteractions();
    }



    @Test
    void createNewProjectStatusIsOK() throws Exception {
        //        given
        ProjectDTO project = getProjectDTO(PROJECT_IDENTIFIER1);
        given(projectService.createNewProject(any(ProjectDTO.class))).willReturn(project);

//        When
        mockMvc.perform(post("/api/v1/projects/new")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectName",equalTo(PROJECT_NAME)))
                .andExpect(jsonPath("$.projectIdentifier",equalTo(PROJECT_IDENTIFIER1)))
                .andExpect(jsonPath("$.startDate",equalTo(null)));
    }

    @Test
    void createNewProjectStatusIs400() throws Exception {
//        given
        Project project = new Project();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);
        project.setProjectIdentifier("Iden8888");
        project.setDescription(DESCRIPTION);


//        When
        mockMvc.perform(post("/api/v1/projects/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(project)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.projectIdentifier", Is.is("Please use 4 to 5 characters long")));

    }

    @Test
    void createNewProjectStatusIs400withEmptyProject() throws Exception {
//        given
        Project project = new Project();
        project.setId(ID);


//        When
        mockMvc.perform(post("/api/v1/projects/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(project)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.projectName", Is.is("Project Name is required")))
                .andExpect(jsonPath("$.projectIdentifier",Is.is("ProjectIdentifier is required")))
                .andExpect(jsonPath("$.description",Is.is("Please provide a description to the project")));

    }

    @Test
    void getProjectByIdentifier(){
//        Given
        ProjectDTO projectDTO = getProjectDTO(PROJECT_IDENTIFIER);
        given(projectService.getProjectByIdentifier(any(String.class))).willReturn(projectDTO);


//        When
       var result = controller.getProjectByIdentifier(PROJECT_IDENTIFIER);

//        Then
        assertEquals(ProjectDTO.class, Objects.requireNonNull(result.getBody()).getClass());
        assertEquals(HttpStatus.OK,result.getStatusCode());
    }

    @Test
    void getProjectByIdentifierStatusIsOk() throws Exception {
        //        Given
        ProjectDTO projectDTO = getProjectDTO(PROJECT_IDENTIFIER1);
        given(projectService.getProjectByIdentifier(any(String.class))).willReturn(projectDTO);

//        Then
        mockMvc.perform(get("/api/v1/projects/Iden")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectIdentifier",equalTo(PROJECT_IDENTIFIER1)))
                .andExpect(jsonPath("$.startDate",equalTo(null)));
    }

    @Test
    void getProjectByIdentifierStatusIs404() throws Exception {
        //        Given
        given(projectService.getProjectByIdentifier(any(String.class))).willThrow(ProjectIdException.class);

//        Then
        mockMvc.perform(get("/api/v1/projects/Idenuuuu")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllProjects(){

//        Given
        List<ProjectDTO> projectDTOS = new ArrayList<>();
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(ID);
        projectDTOS.add(projectDTO);
        ProjectListDTO projectListDTO = new ProjectListDTO(projectDTOS);
        given(projectService.getAllProjects()).willReturn((projectListDTO));

//        When
        var result = controller.getAllProject();

//        Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals(EXPECTED_SIZE, Objects.requireNonNull(result.getBody()).getProjects().size());
        then(projectService).should().getAllProjects();
        then(projectService).shouldHaveNoMoreInteractions();
    }

    @Test
    void getAllProjectStatusOk() throws Exception {
//        given
        List<ProjectDTO> projectDTOS = new ArrayList<>();
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(ID);
        projectDTOS.add(projectDTO);
        ProjectListDTO projectListDTO = new ProjectListDTO(projectDTOS);
        given(projectService.getAllProjects()).willReturn((projectListDTO));

//        then
        mockMvc.perform(get("/api/v1/projects")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projects", hasSize(EXPECTED_SIZE)));
    }


    @Test
    void deleteProject(){
//        Given
        Project project = new Project();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);
        project.setProjectIdentifier(PROJECT_IDENTIFIER1);
//        when
        controller.deleteProject(project.getProjectIdentifier());

//        then
        then(projectService).should().deleteProject(any(String.class));
        then(projectService).shouldHaveNoMoreInteractions();


    }

    @Test
    void deleteProjectStatusOK() throws Exception {
//        Given
        Project project = new Project();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);
        project.setProjectIdentifier(PROJECT_IDENTIFIER1);
//        when
        mockMvc.perform(delete("/api/v1/projects/iden")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",equalTo("Project with ID: IDEN has been deleted successfully")));

    }
    @Test
    void deleteProjectStatus400() throws Exception {
        Project project = new Project();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);
        willThrow(ProjectIdException.class).given(projectService).deleteProject(null);
//        when
        mockMvc.perform(delete("/api/v1/projects/k")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void UpdateProject(){
//        given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setDescription(DESCRIPTION);
        projectDTO.setId(ID);

        ProjectDTO returnedDto = new ProjectDTO();
        returnedDto.setId(projectDTO.getId());
        returnedDto.setDescription(projectDTO.getDescription());
        given(projectService.updateProject(anyLong(),any(ProjectDTO.class))).willReturn(returnedDto);

//        when
        var responseEntity = controller.updateProject(ID,projectDTO);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

    }

    @Test
    void UpdateProjectStatusIsOk() throws Exception {
//        given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setDescription(DESCRIPTION);
        projectDTO.setId(ID);

        ProjectDTO returnedDto = new ProjectDTO();
        returnedDto.setId(projectDTO.getId());
        returnedDto.setDescription(projectDTO.getDescription());
        given(projectService.updateProject(anyLong(),any(ProjectDTO.class))).willReturn(returnedDto);

//        then

        mockMvc.perform(put("/api/v1/projects/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(projectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description",equalTo(DESCRIPTION)));
        then(projectService).should().updateProject(anyLong(),any(ProjectDTO.class));
        then(projectService).shouldHaveNoMoreInteractions();

    }

    @Test
    void UpdateProjectStatusIs400() throws Exception {

//        Given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setDescription(DESCRIPTION);
        projectDTO.setId(ID);


//        Then
        mockMvc.perform(put("/api/v1/projects/kkkk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(projectDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProjectStatus400WhenProjectNotFound(){
//        Given
        given(projectService.updateProject(anyLong(),any(ProjectDTO.class))).willThrow(ProjectNotFoundException.class);
//        when
       var response= controller.updateProject(ID,new ProjectDTO());
//       Then
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    private ProjectDTO getProjectDTO(String projectIdentifier) {
        ProjectDTO project = new ProjectDTO();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);
        project.setProjectIdentifier(projectIdentifier);
        project.setDescription(DESCRIPTION);
        return project;
    }

}