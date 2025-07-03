package com.taskmanagement.service;

import com.taskmanagement.domain.enums.Status;
import com.taskmanagement.domain.models.TaskModel;
import com.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;
    private TaskModel sampleTask;

    @BeforeEach
    void setup() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService();
        taskService.taskRepository = taskRepository;

        sampleTask = new TaskModel("Test Task", "Task description", Status.ToDo);
        sampleTask.setId(1L);
    }

    @Test
    void testCreate() {
        doNothing().when(taskRepository).persist(any(TaskModel.class));

        TaskModel result = taskService.create(sampleTask);

        verify(taskRepository).persist(sampleTask);
        assertNull(result.getId()); // Foi setado como null antes do persist
        assertEquals("Test Task", result.getTitle());
    }

    @Test
    void testUpdate_WhenTaskExists() {
        TaskModel updated = new TaskModel("Updated", "Updated Desc", Status.Completed);
        updated.setId(1L);

        when(taskRepository.findByIdOptional(1L)).thenReturn(Optional.of(sampleTask));

        Optional<TaskModel> result = taskService.update(updated);

        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getTitle());
        assertEquals("Updated Desc", result.get().getDescription());
        assertEquals(Status.Completed, result.get().getStatus());
    }

    @Test
    void testUpdate_WhenTaskDoesNotExist() {
        TaskModel updated = new TaskModel("Updated", "Updated Desc", Status.Completed);
        updated.setId(2L);

        when(taskRepository.findByIdOptional(2L)).thenReturn(Optional.empty());

        Optional<TaskModel> result = taskService.update(updated);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDelete_WhenTaskExists() {
        when(taskRepository.findByIdOptional(1L)).thenReturn(Optional.of(sampleTask));

        boolean result = taskService.delete(1L);

        assertTrue(result);
        verify(taskRepository).delete(sampleTask);
    }

    @Test
    void testDelete_WhenTaskDoesNotExist() {
        when(taskRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        boolean result = taskService.delete(99L);

        assertFalse(result);
        verify(taskRepository, never()).delete(any());
    }

    @Test
    void testListAll() {
        when(taskRepository.listAll()).thenReturn(List.of(sampleTask));

        List<TaskModel> result = taskService.listAll();

        assertEquals(1, result.size());
        assertEquals("Test Task", result.getFirst().getTitle());
    }

    @Test
    void testFindById_WhenExists() {
        when(taskRepository.findByIdOptional(1L)).thenReturn(Optional.of(sampleTask));

        Optional<TaskModel> result = taskService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Task", result.get().getTitle());
    }

    @Test
    void testFindById_WhenNotFound() {
        when(taskRepository.findByIdOptional(123L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.findById(123L));
    }

    @Test
    void testFindByStatus() {
        when(taskRepository.findByStatus(Status.ToDo)).thenReturn(List.of(sampleTask));

        List<TaskModel> result = taskService.findByStatus(Status.ToDo);

        assertEquals(1, result.size());
        assertEquals(Status.ToDo, result.getFirst().getStatus());
    }
}
