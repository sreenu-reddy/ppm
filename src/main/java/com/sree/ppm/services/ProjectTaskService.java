package com.sree.ppm.services;

import com.sree.ppm.api.v1.models.ProjectTaskDTo;

public interface ProjectTaskService {
    ProjectTaskDTo createProjectTask(String projectIdentifier,ProjectTaskDTo projectTaskDTo);
}
