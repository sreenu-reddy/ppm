package com.sree.ppm.api.v1.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sree.ppm.domains.BackLog;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class ProjectTaskDTo {
    private Long id;
    private String projectSequence;
    @NotBlank(message = "Please provide the summary of the projectTask")
    private String summary;
    private String acceptanceCriteria;
    private String status;
    private Integer priority;
    private Date dueDate;
    private String projectIdentifier;
    @JsonIgnore
    private BackLog backLog;
}
