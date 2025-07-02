package com.taskmanagement.service;

import com.taskmanagement.domain.models.TaskModel;
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
        task.id = null;
        taskRepository.persist(task);
        return task;
    }

    @Transactional
    public Optional<TaskModel> update(TaskModel updatedTask) {
        Optional<TaskModel> existing = taskRepository.findByIdOptional(updatedTask.id);

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        TaskModel task = existing.get();
        task.title = updatedTask.title;
        task.description = updatedTask.description;
        task.status = updatedTask.status;

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
        return taskRepository.findByIdOptional(id);
    }
}
