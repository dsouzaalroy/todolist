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

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task.setState(State.TODO);
        task = taskService.createTask(task);
        return ResponseEntity.ok().body(task);
    }

    @PutMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        task = taskService.updateTask(task);
        return ResponseEntity.ok().body(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") Long id) {
        Task task = taskService.getTask(id);
        return ResponseEntity.ok().body(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> tasks = taskService.getTasks();
        return ResponseEntity.ok().body(tasks);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException() {
        // TODO Needs much better error handling
        return ResponseEntity.badRequest()
                .body("Please inspect the request as it was invalid");
    }
}
