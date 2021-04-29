package com.sree.ppm.api.v1.mapper;

import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.domains.ProjectTask;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectTaskMapper{
    ProjectTaskMapper INSTANCE = Mappers.getMapper(ProjectTaskMapper.class);

    ProjectTaskDTo projectTaskToProjectTaskDTO(ProjectTask projectTask);
    ProjectTask projectTaskDTOToProjectTask(ProjectTaskDTo projectTaskDTo);

}
