package com.sree.ppm.api.v1.controller;

import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> createNewProject(@Valid @RequestBody ProjectDTO project, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
             Map<String, String> errorsMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> errorsMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errorsMap,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(projectService.createNewProject(project), HttpStatus.CREATED);
    }


    @GetMapping("/{identifier}")
    public ResponseEntity<?> getProjectByIdentifier(@PathVariable String identifier){
        try {
            ProjectDTO projectDTO = projectService.getProjectByIdentifier(identifier);
            return new ResponseEntity<>(projectDTO,HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
