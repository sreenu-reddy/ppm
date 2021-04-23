package com.sree.ppm.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.domains.Project;
import com.sree.ppm.exceptions.ProjectIdException;
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

import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    ProjectController controller;
    @Mock
    ProjectService projectService;

    MockMvc mockMvc;

    @Mock
    BindingResult bindingResult;

    private final ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        controller = new ProjectController(projectService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createNewProject() throws NoSuchMethodException {
//        given
        ProjectDTO project = new ProjectDTO();
        project.setId(1L);
        project.setProjectName("ProjectName");
        project.setProjectIdentifier("Identifier");
        project.setDescription("Description");
        given(projectService.createNewProject(any(ProjectDTO.class))).willReturn(project);

//        When

        ResponseEntity<Object> project1 = controller.createNewProject(project,bindingResult);

//        then
        assertNotNull(project1);
        assertEquals(HttpStatus.CREATED,project1.getStatusCode());
        assertEquals(project.getClass().getDeclaredMethod("getProjectIdentifier"), Objects.requireNonNull(project1.getBody()).getClass().getDeclaredMethod("getProjectIdentifier"));
        then(projectService).should().createNewProject(any(ProjectDTO.class));
        then(projectService).shouldHaveNoMoreInteractions();
    }

    @Test
    void createNewProjectStatusIsOK() throws Exception {
        //        given
        ProjectDTO project = new ProjectDTO();
        project.setId(1L);
        project.setProjectName("ProjectName");
        project.setProjectIdentifier("Iden");
        project.setDescription("Description");
        given(projectService.createNewProject(any(ProjectDTO.class))).willReturn(project);

//        When
        mockMvc.perform(post("/api/v1/projects/new")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectName",equalTo("ProjectName")))
                .andExpect(jsonPath("$.projectIdentifier",equalTo("Iden")))
                .andExpect(jsonPath("$.startDate",equalTo(null)));
    }

    @Test
    void createNewProjectStatusIs400() throws Exception {
//        given
        Project project = new Project();
        project.setId(1L);
        project.setProjectName("ProjectName");
        project.setProjectIdentifier("Iden8888");
        project.setDescription("Description");


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
        project.setId(1L);


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
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setProjectName("Name");
        projectDTO.setProjectIdentifier("Identifier");
        projectDTO.setDescription("Description");
        given(projectService.getProjectByIdentifier(any(String.class))).willReturn(projectDTO);


//        When
       var result = controller.getProjectByIdentifier("Identifier");

//        Then
        assertEquals(ProjectDTO.class, Objects.requireNonNull(result.getBody()).getClass());
        assertEquals(HttpStatus.OK,result.getStatusCode());
    }

    @Test
    void getProjectByIdentifierStatusIsOk() throws Exception {
        //        Given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setProjectName("Name");
        projectDTO.setProjectIdentifier("Iden");
        projectDTO.setDescription("Description");
        given(projectService.getProjectByIdentifier(any(String.class))).willReturn(projectDTO);

//        Then
        mockMvc.perform(get("/api/v1/projects/Iden")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectIdentifier",equalTo("Iden")))
                .andExpect(jsonPath("$.startDate",equalTo(null)));
    }

    @Test
    void getProjectByIdentifierStatusIs404() throws Exception {
        //        Given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setProjectName("Name");
        projectDTO.setProjectIdentifier("Iden");
        projectDTO.setDescription("Description");
        given(projectService.getProjectByIdentifier(any(String.class))).willThrow(ProjectIdException.class);

//        Then
        mockMvc.perform(get("/api/v1/projects/Idenuuuu")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


    }
}