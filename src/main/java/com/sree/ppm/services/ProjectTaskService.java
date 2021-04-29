package com.sree.ppm.services;

import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.api.v1.models.ProjectTaskListDTO;

public interface ProjectTaskService {
    ProjectTaskDTo createProjectTask(String projectIdentifier,ProjectTaskDTo projectTaskDTo);
    ProjectTaskListDTO getAllProjectTasks(String backLogId);
}