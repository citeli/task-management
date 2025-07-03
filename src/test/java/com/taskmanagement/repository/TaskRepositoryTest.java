package com.taskmanagement.repository;

import com.taskmanagement.domain.enums.Status;
import com.taskmanagement.domain.models.TaskModel;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
class TaskRepositoryTest {

    @Inject
    TaskRepository taskRepository;

    @Test
    @Transactional
    void testFindByStatus_ShouldReturnOnlyCompletedTasks() {
        //objects seeded on TaskDataSeeder
        List<TaskModel> completedTasks = taskRepository.findByStatus(Status.Completed);

        Assertions.assertEquals(2, completedTasks.size());
        Assertions.assertTrue(completedTasks.stream().allMatch(task -> task.getStatus() == Status.Completed));
    }

    @Test
    @Transactional
    void testFindByStatus_ShouldReturnEmptyListWhenNoMatch() {
        List<TaskModel> inProgressTasks = taskRepository.findByStatus(Status.InProgress);
        Assertions.assertTrue(inProgressTasks.isEmpty());
    }
}