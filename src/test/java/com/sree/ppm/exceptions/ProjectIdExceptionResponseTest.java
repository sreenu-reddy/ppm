package com.sree.ppm.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectIdExceptionResponseTest {


    public static final String MESSAGE = "Hey There";

    @Test
    void getProjectIdentifier() {
        //        when
        ProjectIdExceptionResponse response = new ProjectIdExceptionResponse(MESSAGE);
//        then
        assertEquals(MESSAGE,response.getProjectIdentifier());
    }
}