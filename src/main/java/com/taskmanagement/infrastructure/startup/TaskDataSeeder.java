package com.taskmanagement.infrastructure.startup;

import com.taskmanagement.domain.enums.Status;
import com.taskmanagement.domain.models.TaskModel;
import com.taskmanagement.repository.TaskRepository;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Startup
@ApplicationScoped
//@IfBuildProfile("prod")
public class TaskDataSeeder {
    @Inject
    TaskRepository taskRepository;

    @Transactional
    public void onStartup(@Observes StartupEvent event) {
        System.out.println("[TaskDataSeeder] Initializing...");

        if (taskRepository.count() == 0) {
            taskRepository.persist(new TaskModel("Task 1 title", "task 1 description", Status.ToDo));
            taskRepository.persist(new TaskModel("Task 3 title", "task 3 description", Status.Completed));
            taskRepository.persist(new TaskModel("Task 4 title", "task 4 description", Status.Completed));

            System.out.println("[TaskDataSeeder] Seeded");
        }else{
            System.out.println("[TaskDataSeeder] Not Seeded");
        }
    }
}
