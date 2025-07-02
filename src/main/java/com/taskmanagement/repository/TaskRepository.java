package com.taskmanagement.repository;

import com.taskmanagement.domain.enums.Status;
import com.taskmanagement.domain.models.TaskModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TaskRepository implements PanacheRepository<TaskModel> {

    public List<TaskModel> findByStatus(Status status) {
        return list("status", status);
    }
}
