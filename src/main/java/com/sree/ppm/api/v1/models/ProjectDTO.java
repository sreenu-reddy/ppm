package com.sree.ppm.api.v1.models;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class ProjectDTO {
    private Long id;
    @NotBlank(message = "Project Name is required")
    private String projectName;
    @NotBlank(message = "ProjectIdentifier is required")
    @Size(min = 4,max = 5, message = "Please use 4 to 5 characters long")
    private String projectIdentifier;
    @NotBlank(message = "Please provide a description to the project")
    private String description;
    private Date startDate;
    private Date endDate;
}
