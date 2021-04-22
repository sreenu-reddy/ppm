package com.sree.ppm.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ProjectIdException.class)
    protected final ResponseEntity<Object> handleProjectIdExp(ProjectIdException exception){
        ProjectIdExceptionResponse response = new ProjectIdExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}
