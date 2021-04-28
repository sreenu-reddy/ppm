package com.sree.ppm.services;

import com.sree.ppm.api.v1.models.ProjectListDTO;
import com.sree.ppm.domains.BackLog;
import com.sree.ppm.exceptions.ProjectIdException;
import com.sree.ppm.api.v1.mapper.ProjectMapper;
import com.sree.ppm.api.v1.models.ProjectDTO;
import com.sree.ppm.repositories.BackLogRepository;
import com.sree.ppm.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class ProjectServiceImpl implements ProjectService {
   private final ProjectRepository projectRepository;
   private static final ProjectMapper projectMapper = ProjectMapper.INSTANCE;
   private final BackLogRepository backLogRepository;
    public ProjectServiceImpl(ProjectRepository projectRepository, BackLogRepository backLogRepository) {
        this.projectRepository = projectRepository;
        this.backLogRepository = backLogRepository;
    }

    @Override
    public ProjectDTO createNewProject(ProjectDTO projectDTO) {
            try{
                var detachedProject = projectMapper.projectDTOToProject(projectDTO);
                    BackLog backLog = new BackLog();
                    backLog.setProject(detachedProject);
                    backLog.setProjectIdentifier(detachedProject.getProjectIdentifier().toUpperCase());
                    detachedProject.setBackLog(backLog);
                    detachedProject.setProjectIdentifier(detachedProject.getProjectIdentifier().toUpperCase());
                var savedProject = projectRepository.save(detachedProject);
                return  projectMapper.projectToProjectDTO(savedProject);
            }catch (Exception exception){
                throw new ProjectIdException("Project ID :"+projectDTO.getProjectIdentifier().toUpperCase()+" Already Exists");
            }


    }

    @Override
    public ProjectDTO getProjectByIdentifier(String identifier) {
            var project = projectRepository.findByProjectIdentifier(identifier.toUpperCase());
            if(project==null){
                throw new ProjectIdException("Project with ID :"+identifier.toUpperCase()+" does not Exists");
            }
            return projectMapper.projectToProjectDTO(project);
    }

    @Override
    public ProjectListDTO getAllProjects() {

        return new ProjectListDTO(StreamSupport.stream(projectRepository.findAll().spliterator(),false).map(projectMapper::projectToProjectDTO).collect(Collectors.toList()));
    }

    @Override
    public void deleteProject(String projectId) {
        var project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project!=null){
            projectRepository.delete(project);
        }else {
            throw new ProjectIdException("Project with ID :"+projectId.toUpperCase()+" does not Exists");
        }
    }

    @Override
    public ProjectDTO updateProject(Long id,ProjectDTO projectDTO) {

        var detachedProject = projectMapper.projectDTOToProject(projectDTO);
        detachedProject.setId(id);
        detachedProject.setBackLog(backLogRepository.findByProjectIdentifier(detachedProject.getProjectIdentifier().toUpperCase()));
        var savedProject = projectRepository.save(detachedProject);

        return projectMapper.projectToProjectDTO(savedProject);
    }


}
