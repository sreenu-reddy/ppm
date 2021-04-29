package com.sree.ppm.api.v1.controller;

import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.services.ProjectTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/backlog")
public class BackLogController {

    private final ProjectTaskService projectTaskService;

    public BackLogController(ProjectTaskService projectTaskService) {
        this.projectTaskService = projectTaskService;
    }

    @PostMapping("/{backLogId}")
    public ResponseEntity<Object> createProjectTask(@Valid @RequestBody ProjectTaskDTo projectTaskDTo,
                                                            BindingResult bindingResult, @PathVariable String backLogId){
        if (bindingResult.hasErrors()){
            Map<String, String> errorsMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> errorsMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(projectTaskService.createProjectTask(backLogId,projectTaskDTo),HttpStatus.CREATED);
    }
}
