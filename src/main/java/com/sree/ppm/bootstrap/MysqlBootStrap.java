package com.sree.ppm.bootstrap;

import com.sree.ppm.domains.BackLog;
import com.sree.ppm.domains.Project;
import com.sree.ppm.domains.ProjectTask;
import com.sree.ppm.repositories.BackLogRepository;
import com.sree.ppm.repositories.ProjectRepository;
import com.sree.ppm.repositories.ProjectTaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile({"dev", "prod"})
public class MysqlBootStrap  implements CommandLineRunner {

    private final ProjectRepository projectRepository;
    private final BackLogRepository backLogRepository;
    private final ProjectTaskRepository projectTaskRepository;

    public MysqlBootStrap(ProjectRepository projectRepository, BackLogRepository backLogRepository, ProjectTaskRepository projectTaskRepository) {
        this.projectRepository = projectRepository;
        this.backLogRepository = backLogRepository;
        this.projectTaskRepository = projectTaskRepository;
    }



    @Override
    public void run(String... args)  {
        if (projectRepository.count()==0){
            loadFirstProject();
            secondProject();
        }
    }

    private void loadFirstProject(){
        var project = new Project();
        var backLog = new BackLog();
        var projectTask = new ProjectTask();
        var projectTask1 = new ProjectTask();
        project.setProjectIdentifier("FIRST");
        project.setProjectName("First Project");
        project.setDescription("First Description");

        backLog.setProject(project);
        backLog.setProjectIdentifier(project.getProjectIdentifier());
        backLog.setPtSequence(0);
        backLog.getProjectTasks().add(projectTask);
        backLog.getProjectTasks().add(projectTask1);

        projectTask.setSummary("First ProjectTask ");
        projectTask.setBackLog(backLog);
        projectTask.setPriority(1);
        projectTask.setProjectSequence(project.getProjectIdentifier()+"-"+backLog.getPtSequence());
        projectTask.setStatus("to-do");
        projectTask.setProjectIdentifier(backLog.getProjectIdentifier());

        projectTask1.setProjectIdentifier(backLog.getProjectIdentifier());
        projectTask1.setProjectSequence(project.getProjectIdentifier()+"-"+(backLog.getPtSequence()+1));
        projectTask1.setSummary("Second ProjectTask");
        projectTask1.setBackLog(backLog);
        projectTask1.setPriority(2);
        projectTask1.setStatus("to-do");

        projectRepository.save(project);
        backLogRepository.save(backLog);
        projectTaskRepository.save(projectTask);
        projectTaskRepository.save(projectTask1);
    }

    private void secondProject(){
        var project1 = new Project();
        var backLog1 = new BackLog();
        var projectTask2 = new ProjectTask();
        var projectTask1 = new ProjectTask();
        var projectTask3 = new ProjectTask();
        project1.setProjectIdentifier("SEND");
        project1.setProjectName("Second Project");
        project1.setDescription("Second Description");

        backLog1.setProject(project1);
        backLog1.setProjectIdentifier(project1.getProjectIdentifier());
        backLog1.setPtSequence(0);
        backLog1.getProjectTasks().add(projectTask2);
        backLog1.getProjectTasks().add(projectTask1);
        backLog1.getProjectTasks().add(projectTask3);

        projectTask2.setSummary("SF ProjectTask ");
        projectTask2.setBackLog(backLog1);
        projectTask2.setPriority(1);
        projectTask2.setProjectSequence(project1.getProjectIdentifier()+"-"+backLog1.getPtSequence());
        projectTask2.setStatus("to-do");
        projectTask2.setProjectIdentifier(backLog1.getProjectIdentifier());

        projectTask1.setProjectIdentifier(backLog1.getProjectIdentifier());
        projectTask1.setProjectSequence(project1.getProjectIdentifier()+"-"+(backLog1.getPtSequence()+1));
        projectTask1.setSummary("Ss ProjectTask");
        projectTask1.setBackLog(backLog1);
        projectTask1.setPriority(3);
        projectTask1.setStatus("to-do");

        projectTask3.setProjectIdentifier(backLog1.getProjectIdentifier());
        projectTask3.setProjectSequence(project1.getProjectIdentifier()+"-"+(backLog1.getPtSequence()+1));
        projectTask3.setSummary("SThird ProjectTask");
        projectTask3.setBackLog(backLog1);
        projectTask3.setPriority(3);
        projectTask3.setStatus("to-do");
        projectRepository.save(project1);
        backLogRepository.save(backLog1);
        projectTaskRepository.save(projectTask1);
        projectTaskRepository.save(projectTask2);
        projectTaskRepository.save(projectTask3);
    }
}
