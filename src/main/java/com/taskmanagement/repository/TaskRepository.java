package com.taskmanagement.repository;

import com.taskmanagement.domain.models.TaskModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskRepository implements PanacheRepository<TaskModel> {
}
