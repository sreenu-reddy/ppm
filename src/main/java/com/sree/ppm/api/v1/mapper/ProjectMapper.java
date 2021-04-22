package com.sree.ppm.api.v1.mapper;

import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.domains.Project;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectDTO projectToProjectDTO(Project project);
    Project projectDTOToProject(ProjectDTO projectDTO);

}
