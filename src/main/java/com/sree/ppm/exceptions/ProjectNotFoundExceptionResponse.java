package com.sree.ppm.exceptions;

public class ProjectNotFoundExceptionResponse {
    private final String message;

    public ProjectNotFoundExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
