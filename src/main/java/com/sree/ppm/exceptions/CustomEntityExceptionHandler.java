package com.sree.ppm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ProjectIdException.class)
    protected final ResponseEntity<Object> handleProjectIdExp(ProjectIdException exception){
        var response = new ProjectIdExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    protected final ResponseEntity<Object> handleProjectNotFoundExp(ProjectNotFoundException exception){
        var response = new ProjectNotFoundExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


}
