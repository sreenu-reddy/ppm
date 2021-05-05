package com.sree.ppm.api.v1.controller;

import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.api.v1.models.ProjectListDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectControllerIT {

    @LocalServerPort
    private int port;

    private  String baseUrl = "http://localhost";

    @Autowired
    TestRestTemplate testRestTemplate;

@Test
void getAllProjects(){
//        Given
    baseUrl = baseUrl.concat(":").concat(port+"/api/v1/projects");
//    When
    ResponseEntity<ProjectListDTO> listDto = testRestTemplate.getForEntity(baseUrl,ProjectListDTO.class);

//    Then
    assertNotNull(listDto.getBody());
    assertEquals(2,listDto.getBody().getProjects().size());
    assertEquals(HttpStatus.OK,listDto.getStatusCode());
    assertEquals(200,listDto.getStatusCodeValue());

}
    @Test
    void getProjectByIdentifier() {
//        given
        baseUrl = baseUrl.concat(":").concat(port+"/api/v1/projects/{identifier}");

      ProjectDTO DTO= testRestTemplate.getForObject(baseUrl, ProjectDTO.class,"first");

      assertNotNull(DTO);
      assertEquals("FIRST",DTO.getProjectIdentifier());
      assertEquals("first",DTO.getProjectName());
      assertEquals("firstDes",DTO.getDescription());
      assertNull(DTO.getBackLog());
      assertNull(DTO.getStartDate());
      assertNull(DTO.getEndDate());

    }

    @Test
    void createProject(){
//        Given
        baseUrl = baseUrl.concat(":").concat(port+"/api/v1/projects/new");
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectIdentifier("Iden");
        projectDTO.setDescription("des");
        projectDTO.setProjectName("myFirstProject");
//        when
        ResponseEntity<ProjectDTO> responseEntity =  testRestTemplate.postForEntity(baseUrl,projectDTO,ProjectDTO.class);

//        Then
        assertEquals(201,responseEntity.getStatusCodeValue());
        assertEquals("IDEN", Objects.requireNonNull(responseEntity.getBody()).getProjectIdentifier());
        assertEquals("des",responseEntity.getBody().getDescription());
        assertEquals("myFirstProject",responseEntity.getBody().getProjectName());
        assertNull(responseEntity.getBody().getBackLog());
        assertNull(responseEntity.getBody().getEndDate());
        assertNull(responseEntity.getBody().getStartDate());

    }

    @Test
    void deleteProject(){

//    Given
        baseUrl = baseUrl.concat(":").concat(port+"/api/v1/projects/{projectId}");

       testRestTemplate.delete(baseUrl,"first");
   }

}