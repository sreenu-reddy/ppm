package com.sree.ppm.api.v1.controller;

import com.sree.ppm.domains.Project;
import com.sree.ppm.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping(value = "/new",consumes = "application/json",produces = "application/json")
    public ResponseEntity<Object> createNewProject(@Valid @RequestBody Project project, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errorsMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> errorsMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errorsMap,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(projectService.createNewProject(project), HttpStatus.CREATED);
    }
}
