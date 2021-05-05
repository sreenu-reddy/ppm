package com.sree.ppm.api.v1.controller;

import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.api.v1.models.ProjectListDTO;
import com.sree.ppm.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/projects")
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping(value = "/new",consumes = "application/json",produces = "application/json")
    public ResponseEntity<Object> createNewProject(@Valid @RequestBody ProjectDTO project, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("Problem with RequestBody in while creating project");
             Map<String, String> errorsMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> errorsMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errorsMap,HttpStatus.BAD_REQUEST);
        }
        log.debug("Project is created with ID:"+project.getProjectIdentifier().toUpperCase());
        return new ResponseEntity<>(projectService.createNewProject(project), HttpStatus.CREATED);
    }


    @GetMapping("/{identifier}")
    public ResponseEntity<Object> getProjectByIdentifier(@PathVariable String identifier){
        try {
            var projectDTO = projectService.getProjectByIdentifier(identifier);
            log.debug("Getting Project of projectIdentifier:"+identifier.toUpperCase());
            return new ResponseEntity<>(projectDTO,HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ProjectListDTO> getAllProject(){
        log.debug("Returning all the Projects in DB");
        return new ResponseEntity<>(projectService.getAllProjects(),HttpStatus.OK);
    }
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Object> deleteProject(@PathVariable String projectId){
        try {
            projectService.deleteProject(projectId);
            log.debug("Deleting the project with projectIdentifier:"+projectId.toUpperCase());
            return new ResponseEntity<>("Project with ID: "+projectId.toUpperCase()+" has been deleted successfully",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO){
        try{
            log.debug("updating the project with ID:"+id);
            return new ResponseEntity<>(projectService.updateProject(id,projectDTO),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

// Exceptions Handling
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        String type = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        Object value = ex.getValue();
        var message = String.format("'%s' should be a valid '%s' and '%s' isn't",
                name, type, value);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);

    }
}
