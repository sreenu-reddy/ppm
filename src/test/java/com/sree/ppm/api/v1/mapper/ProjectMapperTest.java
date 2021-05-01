package com.sree.ppm.api.v1.mapper;

import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.domains.BackLog;
import com.sree.ppm.domains.Project;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProjectMapperTest {

    public static final long ID = 1L;
    public static final String PROJECT_NAME = "ProjectName";
    public static final String PROJECT_IDENTIFIER = "iden";
    public static final String DESCRIPTION = "desc";
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
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setProjectIdentifier(PROJECT_IDENTIFIER);
        project.setDescription(DESCRIPTION);
        project.setBackLog(new BackLog());


//        When
        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);

//        Then
        assertNotNull(projectDTO);
        assertEquals(ID,projectDTO.getId());
        assertEquals(PROJECT_NAME,projectDTO.getProjectName());
      assertNotNull(projectDTO.getStartDate());
      assertNotNull(projectDTO.getEndDate());
        assertNotNull(projectDTO.getProjectIdentifier());
        assertNotNull(projectDTO.getDescription());
        assertNotNull(projectDTO.getBackLog());
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
        projectDTO.setProjectIdentifier(PROJECT_IDENTIFIER);
        projectDTO.setDescription(DESCRIPTION);
        projectDTO.setBackLog(new BackLog());
        projectDTO.setProjectName(PROJECT_NAME);
        projectDTO.setStartDate(new Date());
        projectDTO.setEndDate(new Date());

//        When
        Project project = projectMapper.projectDTOToProject(projectDTO);

//        Then
        assertNotNull(project);
        assertEquals(ID,project.getId());
        assertEquals(PROJECT_NAME,project.getProjectName());
        assertNotNull(project.getProjectIdentifier());
        assertNotNull(project.getDescription());
        assertNotNull(project.getStartDate());
        assertNotNull(project.getEndDate());
        assertNotNull(project.getBackLog());
    }
}