package com.sree.ppm;

import com.sree.ppm.domains.BackLog;
import com.sree.ppm.domains.Project;
import com.sree.ppm.domains.ProjectTask;
import com.sree.ppm.repositories.BackLogRepository;
import com.sree.ppm.repositories.ProjectRepository;
import com.sree.ppm.repositories.ProjectTaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {

    private final ProjectRepository projectRepository;
    private final BackLogRepository backLogRepository;
    private final ProjectTaskRepository projectTaskRepository;

    public BootStrapData(ProjectRepository projectRepository, BackLogRepository backLogRepository, ProjectTaskRepository projectTaskRepository) {
        this.projectRepository = projectRepository;
        this.backLogRepository = backLogRepository;
        this.projectTaskRepository = projectTaskRepository;
    }

    @Override
    public void run(String... args) {
           loadProject();
    }

    private void loadProject(){
        var project = new Project();
        var backLog = new BackLog();
        project.setProjectName("first");
        project.setProjectIdentifier("FIRST");
        project.setDescription("firstDes");
        project.setBackLog(backLog);
        backLog.setProject(project);
        backLog.setProjectIdentifier(project.getProjectIdentifier());

        var projectTask = new ProjectTask();
        projectTask.setSummary("firstSum");
        projectTask.setBackLog(backLog);
        projectTask.setProjectIdentifier(backLog.getProjectIdentifier());

        var projectTask1 = new ProjectTask();
        projectTask1.setProjectIdentifier(project.getProjectIdentifier());
        projectTask1.setSummary("SecondSum");
        projectTask1.setBackLog(backLog);

        backLog.getProjectTasks().add(projectTask);
        backLog.getProjectTasks().add(projectTask1);

        projectRepository.save(project);
        backLogRepository.save(backLog);
        projectTaskRepository.save(projectTask);
        projectTaskRepository.save(projectTask1);

    }
}
