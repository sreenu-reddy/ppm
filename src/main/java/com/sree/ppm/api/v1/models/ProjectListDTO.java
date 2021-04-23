package com.sree.ppm.api.v1.models;

import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class ProjectListDTO {
    List<ProjectDTO> projects;

    public List<ProjectDTO> getProjects() {
        return projects;
    }

}
