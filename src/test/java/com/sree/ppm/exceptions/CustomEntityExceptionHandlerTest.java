package com.sree.ppm.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;


class CustomEntityExceptionHandlerTest {

    private final String message="Hello";
    ProjectIdExceptionResponse response = new ProjectIdExceptionResponse(message);


    CustomEntityExceptionHandler customEntityExceptionHandler = new CustomEntityExceptionHandler();

    @Test
    void handleProjectIdExp() throws NoSuchFieldException, NoSuchMethodException {
//        When
       ResponseEntity<Object> responseEntity= customEntityExceptionHandler.handleProjectIdExp(new ProjectIdException(message));

//       Then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        assertEquals(response.getClass().getDeclaredField("projectIdentifier"),responseEntity.getBody().getClass().getDeclaredField("projectIdentifier"));
        assertEquals(response.getClass().getDeclaredMethod("getProjectIdentifier"),responseEntity.getBody().getClass().getDeclaredMethod("getProjectIdentifier"));
    }

    @Test
    void handleProjectNotFound(){
//        When
        ResponseEntity<Object> responseEntity = customEntityExceptionHandler.handleProjectNotFoundExp(new ProjectNotFoundException(message));
//         Then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());

    }

}