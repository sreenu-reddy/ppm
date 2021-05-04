package com.sree.ppm.api.v1.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
public class ProjectListDTO {
    List<ProjectDTO> projects;

    public List<ProjectDTO> getProjects() {
        return projects;
    }

}
