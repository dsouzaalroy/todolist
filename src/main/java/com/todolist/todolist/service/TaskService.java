package com.todolist.todolist.service;

import com.todolist.todolist.dto.State;
import com.todolist.todolist.dto.Task;
import com.todolist.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return (List<Task>) taskRepository.findAll();
    }

    public void createTask(Task task) {
        // TODO Could we validate anything here?
        taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task taskToDelete = getTask(id);
        taskRepository.delete(taskToDelete);
    }

    public void updateState(Long id, State state) {
        Task taskToUpdate = getTask(id);
        if (taskToUpdate.getState() != state) {
            taskToUpdate.setState(state);
            taskRepository.save(taskToUpdate);
        }
    }

//    public void updateTask(Task task) {
//        Task taskToUpdate = taskRepository.findById(task.id()).orElse(null);
//        validateTask(taskToUpdate, task.id());
//        if(task.date())
//    }

    public Task getTask(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            throw new IllegalArgumentException("Could not find task with id: " + id);
        }
        return task;
    }


}
