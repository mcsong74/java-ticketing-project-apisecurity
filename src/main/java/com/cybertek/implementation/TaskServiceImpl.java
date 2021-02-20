package com.cybertek.implementation;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.Project;
import com.cybertek.entity.Task;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.mapper.MapperUtil;
import com.cybertek.mapper.ProjectMapper;
import com.cybertek.mapper.TaskMapper;
import com.cybertek.repository.TaskRepository;
import com.cybertek.repository.UserRepository;
import com.cybertek.service.TaskService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private MapperUtil mapperUtil;
//    TaskMapper taskMapper;
//    ProjectMapper projectMapper;
    private UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, MapperUtil mapperUtil, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
    }

    @Override
    public TaskDTO findById(Long id) throws TicketingProjectException {
        Task task = taskRepository.findById(id).orElseThrow(()-> new TicketingProjectException("Task does " +
                "not exists"));
        return mapperUtil.convert(task, new TaskDTO());
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> list = taskRepository.findAll();
        return list.stream().map(obj-> mapperUtil.convert(obj, new TaskDTO())).collect(Collectors.toList());

    }

    @Override
    public TaskDTO save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());
        Task task = mapperUtil.convert(dto, new Task());
        Task savedTask = taskRepository.save(task);
        return mapperUtil.convert(savedTask, new TaskDTO());
    }

    @Override
    public TaskDTO update(TaskDTO dto) throws TicketingProjectException {
        //check the task to update exists in the db
        taskRepository.findById(dto.getId()).orElseThrow(() -> new TicketingProjectException("Task does " +
                "not exists"));
        //convert task dto to task entity
        Task convertedTask = mapperUtil.convert(dto, new Task());

        //below highlighted will be handled in request body
//        convertedTask.setId(task.getId());
//        convertedTask.setTaskStatus(task.getTaskStatus());
//        convertedTask.setAssignedDate(task.getAssignedDate());

        //save the converted task entity to db and return the task
        Task savedTask = taskRepository.save(convertedTask);
        //return the task dto using mapperUtil.convert
        return mapperUtil.convert(savedTask, new TaskDTO());

    }

    @Override
    public void delete(long id) throws TicketingProjectException {
        Task foundTask = taskRepository.findById(id).orElseThrow(() -> new TicketingProjectException("Task does not " +
                "exists"));
        foundTask.setIsDeleted(true);
        taskRepository.save(foundTask);
    }

    @Override
    public int totalNonCompletedTasks(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTasks(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO project) {

        List<TaskDTO> taskDTOS = listAllByProject(project);
        taskDTOS.forEach(taskDTO -> {
            try {
                delete(taskDTO.getId());
            } catch (TicketingProjectException e) {
                e.printStackTrace();
            }
        });
    }


    public List<TaskDTO> listAllByProject(ProjectDTO project){

        List<Task> list = taskRepository.findAllByProject(mapperUtil.convert(project, new Project()));

        return list.stream().map(obj -> {
            return mapperUtil.convert(obj, new TaskDTO());
        }).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) throws TicketingProjectException {

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(Long.parseLong(id)).orElseThrow(()-> new TicketingProjectException("User " +
                "does not exists"));
        List<Task> list = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status,user);
        return list.stream().map(obj->mapperUtil.convert(obj, new TaskDTO())).collect(Collectors.toList());

    }

    @Override
    public List<TaskDTO> listAllTasksByProjectManager() throws TicketingProjectException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName(); //for REST security, id
        User user = userRepository.findById(Long.parseLong(id)).orElseThrow(()->new TicketingProjectException("This " +
                "user does not exists"));
        List<Task> tasks = taskRepository.findAllByProjectAssignedManager(user);
        return tasks.stream().map(obj -> mapperUtil.convert(obj, new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateStatus(TaskDTO dto) throws TicketingProjectException {
        Task task = taskRepository.findById(dto.getId()).orElseThrow(()->new TicketingProjectException("Task does not" +
                " " +
                "exists"));
        task.setTaskStatus(dto.getTaskStatus());
        Task savedTask  = taskRepository.save(task);
        return mapperUtil.convert(task, new TaskDTO());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username);
        List<Task> list = taskRepository.findAllByTaskStatusAndAssignedEmployee(status,user);
        return list.stream().map(obj->mapperUtil.convert(obj, new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> readAllByEmployee(User assignedEmployee) {
        List<Task> tasks = taskRepository.findAllByAssignedEmployee(assignedEmployee);
        return tasks.stream().map(obj->mapperUtil.convert(obj, new TaskDTO())).collect(Collectors.toList());
    }
}
