package com.tasks.TaskManagement.repository;

import com.tasks.TaskManagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}