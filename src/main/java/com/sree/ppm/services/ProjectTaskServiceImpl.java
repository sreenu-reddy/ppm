package com.sree.ppm.services;

import com.sree.ppm.api.v1.mapper.ProjectTaskMapper;
import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.api.v1.models.ProjectTaskListDTO;
import com.sree.ppm.domains.Project;
import com.sree.ppm.exceptions.ProjectNotFoundException;
import com.sree.ppm.repositories.BackLogRepository;
import com.sree.ppm.repositories.ProjectRepository;
import com.sree.ppm.repositories.ProjectTaskRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ProjectTaskServiceImpl implements ProjectTaskService {

    private final ProjectTaskRepository projectTaskRepository;
    private static final ProjectTaskMapper projectTaskMapper = ProjectTaskMapper.INSTANCE;
    private final BackLogRepository backLogRepository;
    private final ProjectRepository projectRepository;

    public ProjectTaskServiceImpl(ProjectTaskRepository projectTaskRepository, BackLogRepository backLogRepository, ProjectRepository projectRepository) {
        this.projectTaskRepository = projectTaskRepository;
        this.backLogRepository = backLogRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectTaskDTo createProjectTask(String projectIdentifier,ProjectTaskDTo projectTaskDTo) {
        try {
            var detachedProject = projectTaskMapper.projectTaskDTOToProjectTask(projectTaskDTo);
            var backLog = backLogRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
            detachedProject.setBackLog(backLog);
            Integer backLogSequence = backLog.getPTSequence();
            backLogSequence++;
            backLog.setPTSequence(backLogSequence);
            detachedProject.setProjectSequence(backLog.getProjectIdentifier()+"-"+backLogSequence);
            detachedProject.setProjectIdentifier(projectIdentifier.toUpperCase());
            if (detachedProject.getPriority()==null){
                detachedProject.setPriority(0);
            }
            if (detachedProject.getStatus()==null || detachedProject.getStatus().equals("")){
                detachedProject.setStatus("To-Do");
            }
            var savedTask =projectTaskRepository.save(detachedProject);
            return projectTaskMapper.projectTaskToProjectTaskDTO(savedTask);

        }catch (Exception e){
                throw new ProjectNotFoundException("Project Not Found");
        }

    }

    @Override
    public ProjectTaskListDTO getAllProjectTasks(String backLogId) {

        Project project = projectRepository.findByProjectIdentifier(backLogId);
        if (project==null){
            throw new ProjectNotFoundException("Project with BackLogId: "+backLogId.toUpperCase()+" does not exists");
        }else {
            return new ProjectTaskListDTO(projectTaskRepository.findByProjectIdentifierOrderByPriority(backLogId).stream()
                    .map(projectTaskMapper::projectTaskToProjectTaskDTO).collect(Collectors.toList()));
        }
    }
}
