package com.todolist.todolist.controller;

import com.todolist.todolist.dto.Task;
import com.todolist.todolist.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskController = new TaskController(taskService);
    }

    @Test
    void shouldCreateTaskIsSuccessful() {
        // given
        Task task = new Task();

        //when
        Mockito.verify(taskService).createTask(task);

        taskController.createTask(task);
    }

}