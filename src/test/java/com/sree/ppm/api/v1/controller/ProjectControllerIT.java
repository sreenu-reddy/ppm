package com.sree.ppm.api.v1.controller;

import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.api.v1.models.ProjectListDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectControllerIT {

    @LocalServerPort
    private int port;

    private  String baseUrl = "http://localhost";

    @Autowired
    TestRestTemplate testRestTemplate;

    @BeforeEach
    void setUp() {

    }


@Test
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
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

}