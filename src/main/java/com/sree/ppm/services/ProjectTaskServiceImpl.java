package com.sree.ppm.services;

import com.sree.ppm.api.v1.mapper.ProjectTaskMapper;
import com.sree.ppm.api.v1.models.ProjectTaskDTo;
import com.sree.ppm.domains.BackLog;
import com.sree.ppm.domains.ProjectTask;
import com.sree.ppm.exceptions.ProjectNotFoundException;
import com.sree.ppm.repositories.BackLogRepository;
import com.sree.ppm.repositories.ProjectTaskRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskServiceImpl implements ProjectTaskService {

    private final ProjectTaskRepository projectTaskRepository;
    private static final ProjectTaskMapper projectTaskMapper = ProjectTaskMapper.INSTANCE;
    private final BackLogRepository backLogRepository;

    public ProjectTaskServiceImpl(ProjectTaskRepository projectTaskRepository, BackLogRepository backLogRepository) {
        this.projectTaskRepository = projectTaskRepository;
        this.backLogRepository = backLogRepository;
    }

    @Override
    public ProjectTaskDTo createProjectTask(String projectIdentifier,ProjectTaskDTo projectTaskDTo) {
        try {
            ProjectTask detachedProject = projectTaskMapper.projectTaskDTOToProjectTask(projectTaskDTo);
            BackLog backLog = backLogRepository.findByProjectIdentifier(projectIdentifier);
            detachedProject.setBackLog(backLog);
            Integer backLogSequence = backLog.getPTSequence();
            backLogSequence++;
            backLog.setPTSequence(backLogSequence);
            detachedProject.setProjectSequence(backLog.getProjectIdentifier()+"-"+backLogSequence);
            detachedProject.setProjectIdentifier(projectIdentifier);
            if (detachedProject.getPriority()==null){
                detachedProject.setPriority(0);
            }
            if (detachedProject.getStatus()==null || detachedProject.getStatus().equals("")){
                detachedProject.setStatus("To-Do");
            }
            ProjectTask savedTask =projectTaskRepository.save(detachedProject);
            return projectTaskMapper.projectTaskToProjectTaskDTO(savedTask);

        }catch (Exception e){
                throw new ProjectNotFoundException("Project Not Found");
        }

    }
}
