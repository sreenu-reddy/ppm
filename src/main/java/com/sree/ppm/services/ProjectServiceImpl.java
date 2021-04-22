package com.sree.ppm.services;


import com.sree.ppm.exceptions.ProjectIdException;
import com.sree.ppm.api.v1.mapper.ProjectMapper;
import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.repositories.ProjectRepository;
import org.springframework.stereotype.Service;


@Service
public class ProjectServiceImpl implements ProjectService {
   private final ProjectRepository projectRepository;
   private final static ProjectMapper projectMapper = ProjectMapper.INSTANCE;
    public ProjectServiceImpl( ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectDTO createNewProject(ProjectDTO projectDTO) {
            try{
                var detachedProject = projectMapper.projectDTOToProject(projectDTO);
                detachedProject.setProjectIdentifier(detachedProject.getProjectIdentifier().toUpperCase());
                var savedProject = projectRepository.save(detachedProject);
                return  projectMapper.projectToProjectDTO(savedProject);
            }catch (Exception exception){
                throw new ProjectIdException("Project ID :"+projectDTO.getProjectIdentifier().toUpperCase()+" Already Exists");
            }


    }
}
