package com.todolist.todolist.service;

import com.todolist.todolist.dto.State;
import com.todolist.todolist.dto.Task;
import com.todolist.todolist.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
        expectedTask.setId(1L);
        expectedTask.setDescription("I am a task");
        expectedTask.setState(State.READY);
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
        try {
            Task actualTask = taskService.createTask(expectedTask);
            fail("Should rethrow IllegalArgument Exception");
        } catch (IllegalArgumentException _) {
        }
    }

}