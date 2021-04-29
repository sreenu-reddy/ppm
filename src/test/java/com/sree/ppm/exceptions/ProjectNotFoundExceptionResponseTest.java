package com.sree.ppm.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectNotFoundExceptionResponseTest {

    @Test
    void getMessage() {
//        Given
        String message = "hello";
//        When
        ProjectNotFoundExceptionResponse response = new ProjectNotFoundExceptionResponse(message);
//        Then
        assertEquals(message,response.getMessage());
    }
}