package com.todolist.todolist.service;

import com.todolist.todolist.dto.Task;
import com.todolist.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return (List<Task>) taskRepository.findAll();
    }

    public Task createTask(Task task) {
        // TODO Could we validate anything here?
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task taskToDelete = getTask(id);
        taskRepository.delete(taskToDelete);
    }

    public Task updateTask(Task task) {
        Task taskToUpdate = getTask(task.getId());
        taskToUpdate.setState(task.getState() != null ? task.getState() : taskToUpdate.getState());
        taskToUpdate.setDescription(task.getDescription() != null ? task.getDescription() : taskToUpdate.getDescription());
        taskToUpdate.setState(task.getState() != null ? task.getState() : taskToUpdate.getState());
        taskToUpdate.setTitle(task.getTitle() != null ? task.getTitle() : taskToUpdate.getTitle());
        taskToUpdate.setDueDate(task.getDueDate() != null ? task.getDueDate() : taskToUpdate.getDueDate());
        return taskRepository.save(taskToUpdate);
    }

    public Task getTask(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            throw new IllegalArgumentException("Could not find task with id: " + id);
        }
        return task;
    }


}
