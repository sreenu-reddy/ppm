package com.sree.ppm.Exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class CustomEntityExceptionHandlerTest {

    CustomEntityExceptionHandler customEntityExceptionHandler = new CustomEntityExceptionHandler();

    @Test
    void handleProjectIdExp() {
//        Given
        String message ="Hello";


//        When
       ResponseEntity<Object> responseEntity= customEntityExceptionHandler.handleProjectIdExp(new ProjectIdException(message));

//       Then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
    }
}