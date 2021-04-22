package com.sree.ppm.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.domains.Project;
import com.sree.ppm.services.ProjectService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
    void createNewProject() {
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
                .andExpect(jsonPath("$.projectName",equalTo("ProjectName")));
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
        project.setProjectName("");
        project.setDescription("");


//        When
        mockMvc.perform(post("/api/v1/projects/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(project)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.projectName", Is.is("Project Name is required")))
                .andExpect(jsonPath("$.projectIdentifier",Is.is("ProjectIdentifier is required")))
                .andExpect(jsonPath("$.description",Is.is("Please provide a description to the project")));

    }
}