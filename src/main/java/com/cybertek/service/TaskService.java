package com.cybertek.service;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.Task;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;

import java.util.List;

public interface TaskService {
    TaskDTO findById(Long taskId);
    List<TaskDTO> listAllTasks();
    Task save(TaskDTO dto);

    void update(TaskDTO dto);
    void delete(Long taskId);

    int totalCountNonCompletedTasks(String projectCode);
    int totalCountCompletedTasks(String projectCode);

    List<TaskDTO> listAllTasksByProject(ProjectDTO project);
    void deleteByProject(ProjectDTO project);

    List<TaskDTO> listAllTasksByStatusIsNot(Status status);

    List<TaskDTO> listAllTasksByProjectManager();

    void updateStatus (TaskDTO dto);

    List<TaskDTO> listAllTasksByStatus(Status status);

    List<TaskDTO> readAllByEmployee(User assignedEmployee);
}
