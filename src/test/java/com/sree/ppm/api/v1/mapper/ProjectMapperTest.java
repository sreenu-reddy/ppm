package com.sree.ppm.api.v1.mapper;

import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.domains.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectMapperTest {

    public static final long ID = 1L;
    public static final String PROJECT_NAME = "ProjectName";
    ProjectMapper projectMapper = ProjectMapper.INSTANCE;

    @Test
    void projectToProjectDTOReturnsNull() {
        assertNull(projectMapper.projectToProjectDTO(null));
    }

    @Test
    void projectToProjectDTOReturnsEmptyObject(){
        assertNotNull(projectMapper.projectToProjectDTO(new Project()));
    }

    @Test
    void projectToProjectDTO(){
//        Given
        Project project = new Project();
        project.setId(ID);
        project.setProjectName(PROJECT_NAME);

//        When
        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);

//        Then
        assertNotNull(projectDTO);
        assertEquals(ID,projectDTO.getId());
        assertEquals(PROJECT_NAME,projectDTO.getProjectName());
        assertNull(projectDTO.getEndDate());
        assertNull(projectDTO.getStartDate());
        assertNull(projectDTO.getProjectIdentifier());
        assertNull(projectDTO.getDescription());
    }

    @Test
    void projectDTOToProjectReturnsNull() {
        assertNull(projectMapper.projectDTOToProject(null));
    }

    @Test
    void projectDTOToProjectReturnsEmptyObject(){
        assertNotNull(projectMapper.projectDTOToProject(new ProjectDTO()));
    }

    @Test
    void projectDTOToProject(){
//        Given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(ID);
        projectDTO.setProjectName(PROJECT_NAME);

//        When
        Project project = projectMapper.projectDTOToProject(projectDTO);

//        Then
        assertNotNull(project);
        assertEquals(ID,project.getId());
        assertEquals(PROJECT_NAME,project.getProjectName());
        assertNull(project.getProjectIdentifier());
        assertNull(project.getDescription());
        assertNull(project.getStartDate());
        assertNull(project.getEndDate());
    }
}