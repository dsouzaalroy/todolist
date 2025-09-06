package com.todolist.todolist.repository;

import com.todolist.todolist.dto.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
