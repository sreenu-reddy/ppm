package com.sree.ppm.Exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectIdExceptionResponseTest {



    @Test
    void getResponse(){
//        given
        String message = "Hello";
//        When
        ProjectIdExceptionResponse response = new ProjectIdExceptionResponse(message);
//        Then
        assertNotNull(response);
        assertEquals("Hello",response.getProjectIdentifier());
    }

}