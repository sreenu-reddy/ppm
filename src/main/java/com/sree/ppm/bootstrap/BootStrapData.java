package com.sree.ppm.bootstrap;

import com.sree.ppm.domains.BackLog;
import com.sree.ppm.domains.Project;
import com.sree.ppm.domains.ProjectTask;
import com.sree.ppm.repositories.BackLogRepository;
import com.sree.ppm.repositories.ProjectRepository;
import com.sree.ppm.repositories.ProjectTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
@Slf4j
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
        log.debug("Loading BootStrap Data in Default profile");
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
        projectTask.setProjectSequence("FIRST-0");

        var projectTask1 = new ProjectTask();
        projectTask1.setProjectIdentifier(project.getProjectIdentifier());
        projectTask1.setSummary("SecondSum");
        projectTask1.setBackLog(backLog);

        backLog.getProjectTasks().add(projectTask);
        backLog.getProjectTasks().add(projectTask1);

        var project1 = new Project();
        var backLog1 = new BackLog();
        project1.setProjectName("Second");
        project1.setDescription("description");
        project1.setProjectIdentifier("SEE1");
        project1.setBackLog(backLog1);


        backLog1.setProject(project1);
        backLog1.setProjectIdentifier(project1.getProjectIdentifier());

        var projectTask2 =new ProjectTask();
        projectTask2.setProjectSequence(backLog1.getProjectIdentifier()+"-"+0);
        projectTask2.setSummary("Summary_2");
        projectTask2.setBackLog(backLog1);
        projectTask2.setProjectIdentifier(backLog1.getProjectIdentifier());

        backLog1.getProjectTasks().add(projectTask2);

        projectRepository.save(project);
        projectRepository.save(project1);
        backLogRepository.save(backLog);
        backLogRepository.save(backLog1);
        projectTaskRepository.save(projectTask);
        projectTaskRepository.save(projectTask1);
        projectTaskRepository.save(projectTask2);
    }
}
