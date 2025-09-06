package com.todolist.todolist.controller;

import com.todolist.todolist.dto.State;
import com.todolist.todolist.dto.Task;
import com.todolist.todolist.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {
    private TaskController taskController;
    private Task expectedTask;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        taskController = new TaskController(taskService);
        expectedTask = new Task();
        expectedTask.setId(1L);
        expectedTask.setDescription("I am a task");
    }

    @Test
    void shouldCreateTaskIfSuccessful() {
        // when
        when(taskService.createTask(expectedTask)).then(AdditionalAnswers.returnsFirstArg());
        ResponseEntity<Task> response = taskController.createTask(expectedTask);
        Task actualTask = response.getBody();

        // then
        verify(taskService).createTask(expectedTask);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(actualTask);
        assertThat(actualTask.getDescription()).isEqualTo(expectedTask.getDescription());
        assertThat(actualTask.getId()).isEqualTo(expectedTask.getId());
        assertThat(actualTask.getState()).isEqualTo(State.READY);
    }

    @Test
    void shouldGetTaskSuccessfully() {
       // when
        when(taskService.getTask(1L)).thenReturn(expectedTask);
        ResponseEntity<Task> response = taskController.getTask(1L);
        Task actualTask = response.getBody();

        // then
        verify(taskService).getTask(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualTask).isEqualTo(expectedTask);
    }

    @Test
    void shouldGetMultipleTasksSuccessfully() {
        // given
        Task task1 = new Task();
        task1.setId(1L);
        Task task2 = new Task();
        task2.setId(2L);

        // when
        when(taskService.getTasks()).thenReturn(List.of(task1, task2));
        ResponseEntity<List<Task>> response = taskController.getTasks();

        Task actualTask1 = response.getBody().getFirst();
        Task actualTask2 = response.getBody().getLast();

        // then
        verify(taskService).getTasks();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualTask1).isEqualTo(task1);
        assertThat(actualTask2).isEqualTo(task2);
    }

    @Test
    void shouldUpdateTaskIfSuccessful() {
        // given
        when(taskService.updateTask(expectedTask)).then(AdditionalAnswers.returnsFirstArg());

        // when
        ResponseEntity<Task> response = taskController.updateTask(expectedTask);
        Task actualTask = response.getBody();

        // then
        verify(taskService).updateTask(expectedTask);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(actualTask);
        assertThat(actualTask.getDescription()).isEqualTo(expectedTask.getDescription());
        assertThat(actualTask.getId()).isEqualTo(expectedTask.getId());
    }

}