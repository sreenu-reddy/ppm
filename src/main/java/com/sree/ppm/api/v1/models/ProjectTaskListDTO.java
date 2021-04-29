package com.sree.ppm.api.v1.models;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ProjectTaskListDTO {
    List<ProjectTaskDTo> projectTaskDTos;

    public List<ProjectTaskDTo> getProjectTaskDTos() {
        return projectTaskDTos;
    }
}
