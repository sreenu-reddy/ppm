package com.sree.ppm.api.v1.controller;

import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.services.ProjectTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/backlog")
public class BackLogController {

    private final ProjectTaskService projectTaskService;

    public BackLogController(ProjectTaskService projectTaskService) {
        this.projectTaskService = projectTaskService;
    }

    @PostMapping(value = "/{backLogId}")
    public ResponseEntity<Object> createProjectTask(@Valid @RequestBody ProjectTaskDTo projectTaskDTo,
                                                            BindingResult bindingResult, @PathVariable String backLogId){
        if (bindingResult.hasErrors()){
            log.error("Problem with RequestBody in while creating projectTask");
            Map<String, String> errorsMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> errorsMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(projectTaskService.createProjectTask(backLogId,projectTaskDTo),HttpStatus.CREATED);
    }

    @GetMapping("/{backLogId}")
    public ResponseEntity<Object> getBackLogById(@PathVariable String backLogId ){
        try {
            log.debug("Returning BackLog with ID: "+backLogId.toUpperCase());
            return new ResponseEntity<>(projectTaskService.getAllProjectTasks(backLogId),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{backLogID}/{ptSeq}")
    public ResponseEntity<Object> getProjectTaskByProjectSeq(@PathVariable String backLogID,@PathVariable String ptSeq){
        try {
            log.debug("Returning projectTask with given BacklogId:"+backLogID.toUpperCase()+ "And ProjectSeq "+ptSeq.toUpperCase());
            return new ResponseEntity<>(projectTaskService.getProjectTaskByProjectSeq(backLogID,ptSeq),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{backLogID}/{ptSeq}")
    public ResponseEntity<Object> updateProjectTask(@Valid @RequestBody ProjectTaskDTo updatedProjectTask,@PathVariable String backLogID,@PathVariable String ptSeq){
        try{
            log.debug("updating the projectTask of projectSeq:"+ptSeq.toUpperCase()+"of Backlog"+backLogID.toUpperCase());
            return new ResponseEntity<>(projectTaskService.updateProjectByProjectSeq(updatedProjectTask,backLogID,ptSeq),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{backlogId}/{ptId}")
    public ResponseEntity<Object> deleteProjectTask(@PathVariable String backlogId, @PathVariable String ptId){
        log.debug("Deleting ProjectTask with projectSeq:"+ptId.toUpperCase()+"of backLog:"+backlogId.toUpperCase());
        projectTaskService.deleteProjectSeq(backlogId, ptId);

        return new ResponseEntity<>("Project Task "+ptId+" was deleted successfully", HttpStatus.OK);
    }
}
