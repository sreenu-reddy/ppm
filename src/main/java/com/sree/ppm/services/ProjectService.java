package com.sree.ppm.services;


import com.sree.ppm.api.v1.models.ProjectDTO;


public interface ProjectService {
   ProjectDTO createNewProject(ProjectDTO project);
   ProjectDTO getProjectByIdentifier(String identifier);
}
