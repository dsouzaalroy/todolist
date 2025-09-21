package com.todolist.todolist.service;

import com.todolist.todolist.dto.State;
import com.todolist.todolist.dto.Task;
import com.todolist.todolist.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;
    private Task expectedTask;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        taskService = new TaskService(taskRepository);

        expectedTask = new Task();
        expectedTask.setTitle("I am a Title");
        expectedTask.setId(1L);
        expectedTask.setDescription("I am a description");
        expectedTask.setState(State.TODO);
        expectedTask.setDueDate(LocalDate.MAX);
    }

    @Test
    void shouldCreateTask() {
        // when
        when(taskRepository.save(expectedTask)).then(AdditionalAnswers.returnsFirstArg());
        Task actualTask = taskService.createTask(expectedTask);

        // then
        assertNotNull(actualTask);
        assertThat(actualTask).isEqualTo(expectedTask);
    }

    @Test
    void shouldRethrowExceptionOnFailure() {
        // when
        when(taskRepository.save(expectedTask)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(expectedTask),
                "Should throw IllegalArgumentException");
    }

    @Test
    void shouldUpdateAllFieldsSuccessfully() {
        // given
        Task update = new Task();
        update.setId(1L);
        update.setTitle("New Title");
        update.setDescription("New Description");
        update.setState(State.DONE);
        update.setDueDate(java.time.LocalDate.of(2025, 9, 6));

        // when
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(expectedTask));
        when(taskRepository.save(any(Task.class))).then(AdditionalAnswers.returnsFirstArg());

        // then
        Task updated = taskService.updateTask(update);
        assertEquals("New Title", updated.getTitle());
        assertEquals("New Description", updated.getDescription());
        assertEquals(State.DONE, updated.getState());
        assertEquals(java.time.LocalDate.of(2025, 9, 6), updated.getDueDate());
    }

    @Test
    void shouldUpdateOnlyNonNullFields() {
        Task update = new Task();
        update.setId(1L);
        update.setTitle(null); // should not overwrite
        update.setDescription("Updated Description");
        update.setState(State.DONE);
        update.setDueDate(null); // should not overwrite

        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(expectedTask));
        when(taskRepository.save(any(Task.class))).then(AdditionalAnswers.returnsFirstArg());

        Task updated = taskService.updateTask(update);
        assertEquals(expectedTask.getTitle(), updated.getTitle());
        assertEquals(update.getDescription(), updated.getDescription());
        assertEquals(update.getState(), updated.getState());
        assertEquals(expectedTask.getDueDate(), updated.getDueDate());
    }

    @Test
    void shouldThrowExceptionIfTaskNotFound() {
        Task update = new Task();
        update.setId(999L);
        when(taskRepository.findById(999L)).thenReturn(java.util.Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(update));
    }

    @Test
    void shouldThrowExceptionIfIdIsNull() {
        Task update = new Task();
        update.setId(null);
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(update));
    }
}