package com.taskmanagement.service;

import com.taskmanagement.domain.enums.Status;
import com.taskmanagement.domain.models.TaskModel;
import com.taskmanagement.exceptions.TaskNotFoundException;
import com.taskmanagement.repository.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    @Transactional
    public TaskModel create(TaskModel task) {
        task.setId(null);
        taskRepository.persist(task);
        return task;
    }

    @Transactional
    public Optional<TaskModel> update(TaskModel updatedTask) {
        Optional<TaskModel> existing = taskRepository.findByIdOptional(updatedTask.getId());

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        TaskModel task = existing.get();
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());

        return Optional.of(task);
    }

    @Transactional
    public boolean delete(Long id) {
        Optional<TaskModel> existing = taskRepository.findByIdOptional(id);

        if (existing.isEmpty()) {
            return false;
        }

        taskRepository.delete(existing.get());
        return true;
    }

    public List<TaskModel> listAll() {
        return taskRepository.listAll();
    }

    public Optional<TaskModel> findById(Long id) {
        return Optional.ofNullable(taskRepository.findByIdOptional(id)
                .orElseThrow(() -> new TaskNotFoundException(id)));
    }

    public List<TaskModel> findByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }
}
