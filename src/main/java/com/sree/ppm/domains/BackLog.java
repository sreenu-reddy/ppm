package com.sree.ppm.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class BackLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer PTSequence=0;
    private String projectIdentifier;

//    One to one with project
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId",nullable = false)
    @JsonIgnore
    private Project project;
//    one to many with projectTask
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "backLog")
    private List<ProjectTask> projectTasks = new ArrayList<>();

}
