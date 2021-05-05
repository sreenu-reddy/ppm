package com.sree.ppm.api.v1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.api.v1.models.ProjectListDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
//import org.springframework.test.annotation.DirtiesContext;
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

   private final HttpHeaders httpHeaders = new HttpHeaders();


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
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
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
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
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
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deleteProject(){

//    Given
        baseUrl = baseUrl.concat(":").concat(port+"/api/v1/projects/{projectId}");

       testRestTemplate.delete(baseUrl,"first");
}

   @Test
//   @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
   void updateProject() throws JsonProcessingException {
//    Given
       baseUrl = baseUrl.concat(":").concat(port+"/api/v1/projects/first");
       ProjectDTO projectDTO = new ProjectDTO();
       projectDTO.setDescription("des");
       projectDTO.setProjectName("myFirstProject");
       ObjectMapper mapper = new ObjectMapper();
       String requestBody = mapper.writeValueAsString(projectDTO);
       HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
       ResponseEntity<String> responseEntity = testRestTemplate.exchange(
              baseUrl,
               HttpMethod.PUT, httpEntity, String.class);

//       When
       assertNotNull(responseEntity.getBody());
       assertEquals(200,responseEntity.getStatusCodeValue());
   }

}