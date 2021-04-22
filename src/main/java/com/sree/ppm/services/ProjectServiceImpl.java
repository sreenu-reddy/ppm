package com.sree.ppm.services;


import com.sree.ppm.Exceptions.ProjectIdException;
import com.sree.ppm.domains.Project;
import com.sree.ppm.repositories.ProjectRepository;
import org.springframework.stereotype.Service;


@Service
public class ProjectServiceImpl implements ProjectService {
   private final ProjectRepository projectRepository;
    public ProjectServiceImpl( ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project createNewProject(Project project) {
        if (project!=null){
            try{
                project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
                return  projectRepository.save(project);
            }catch (Exception exception){
                throw new ProjectIdException("Project ID :"+project.getProjectIdentifier().toUpperCase()+" Already Exists");
            }


        }else{
            throw new NullPointerException("project is a Null object");
        }

    }
}
