package com.todolist.todolist.controller;

import com.todolist.todolist.dto.State;
import com.todolist.todolist.dto.Task;
import com.todolist.todolist.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController (TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task.setState(State.READY);
        try {
            taskService.createTask(task);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") Long id) {
        Task task;

        try {
            task = taskService.getTask(id);
        }
        catch (IllegalArgumentException e) {
            // TODO Send error message back
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTask() {
        List<Task> tasks;
        try {
            tasks = taskService.getTasks();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(tasks);
    }

}
