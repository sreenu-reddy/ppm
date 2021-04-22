package com.sree.ppm.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project Name is required")
    private String projectName;

    @NotBlank(message = "ProjectIdentifier is required")
    @Size(min = 4,max = 5, message = "Please use 4 to 5 characters long")
    @Column(updatable = false,unique = true)
    private String projectIdentifier;

    @Lob
    @NotBlank(message = "Please provide a description to the project")
    private String description;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date start_date;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date end_date;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date created_At;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date updated_At;

    @PrePersist
    protected void onCreated(){
        this.created_At = new Date();
    }

    @PreUpdate
    protected void onUpdated(){
        this.updated_At = new Date();
    }
}
