package com.cybertek.controller;

import com.cybertek.annotation.DefaultExceptionMessage;
import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.ResponseWrapper;
import com.cybertek.enums.Status;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.service.ProjectService;
import com.cybertek.service.TaskService;
import com.cybertek.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.graalvm.compiler.word.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task")
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {

    private TaskService taskService;
    private ProjectService projectService;

    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    @GetMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong to retrieve all task, please try again!")
    @Operation(summary = "Read all tasks")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> readAll(){

        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved all tasks",
                taskService.listAllTasks()));
    }

    @GetMapping("/project-manager") //spring security has metadata user logged in, so no need path variable
    @DefaultExceptionMessage(defaultMessage = "Something went wrong to retrieve all task by project manager, please " +
            "try again!")
    @Operation(summary = "Read all task by project manager")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> readAllByProjectManager() throws TicketingProjectException {
        List<TaskDTO> taskList = taskService.listAllTasksByProjectManager();
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved task by project manager",
                taskList));
    }

    @GetMapping("/{id}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong to retrieve a task by task id, please " +
            "try again!")
    @Operation(summary = "Read a task by task id")
    @PreAuthorize("hasAnyAuthority('Manager', 'Employee')")
    public ResponseEntity<ResponseWrapper> readById(@PathVariable("id") Long id) throws TicketingProjectException {
        TaskDTO currentTask = taskService.findById(id);
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved the task by task id", currentTask));
    }


    @PostMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong to create a new task, please " +
            "try again!")
    @Operation(summary = "Create a new task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> create(@RequestBody TaskDTO taskDTO){
        TaskDTO createdTask = taskService.save(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Successfully created a new task!", createdTask));
    }

    @DeleteMapping("/{id}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong to delete a task, please " +
            "try again!")
    @Operation(summary = "Delete a task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable("id") Long id) throws TicketingProjectException {
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Successfully task deleted"));
    }

    @PutMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong to update a new task, please " +
            "try again!")
    @Operation(summary = "Update a new task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO) throws TicketingProjectException {
        TaskDTO updatedTask = taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Successfully updated task", updatedTask));
    }

    @GetMapping("/employee")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong to read all non completed task for the employee")
    @Operation(summary = "Read all non completed task")
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<ResponseWrapper> employeeReadAllNonCompletedTask() throws TicketingProjectException {
        List<TaskDTO> taskList = taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Successfully read non completed current user tasks", taskList));

    }

    @PutMapping("/employee/update")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong to update task status")
    @Operation(summary = "Update task status")
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<ResponseWrapper> employeeUpdateTask(@RequestBody TaskDTO taskDTO) throws TicketingProjectException {
        TaskDTO task = taskService.updateStatus(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Successfully employee task status updated", task));
    }



//    TaskService taskService;
//    ProjectService projectService;
//    UserService userService;
//
//    public TaskController(TaskService taskService, ProjectService projectService, UserService userService) {
//        this.taskService = taskService;
//        this.projectService = projectService;
//        this.userService = userService;
//    }
//
//    //
//
//    @GetMapping("/create")
//    public String createTask(Model model){
//
//        model.addAttribute("task",new TaskDTO());
//        model.addAttribute("projects",projectService.listAllNonCompletedProjects());
//        model.addAttribute("employees",userService.listAllByRole("employee"));
//        model.addAttribute("tasks",taskService.listAllTasks());
//
//        return "task/create";
//    }
//
//    @PostMapping("/create")
//    public String insertTask(Model model,TaskDTO task){
//
//        taskService.save(task);
//
//        return "redirect:/task/create";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteTask(@PathVariable("id") Long id){
//        taskService.delete(id);
//        return "redirect:/task/create";
//    }
//
//    @GetMapping("/update/{id}")
//    public String editTask(@PathVariable("id") Long id,Model model){
//
//        model.addAttribute("task",taskService.findById(id));
//        model.addAttribute("projects",projectService.listAllNonCompletedProjects());
//        model.addAttribute("employees",userService.listAllByRole("employee"));
//        model.addAttribute("tasks",taskService.listAllTasks());
//
//        return "task/update";
//    }
//
//    @PostMapping("/update/{id}")
//    public String updateTask(TaskDTO task){
//
//        taskService.update(task);
//
//        return "redirect:/task/create";
//    }
//
//    @GetMapping("/employee")
//    public String edit(Model model){
//
//        List<TaskDTO> tasks = taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
//        model.addAttribute("tasks",tasks);
//
//        return "task/employee-tasks";
//
//    }
//
//    @GetMapping("/employee/edit/{id}")
//    public String employee_update(@PathVariable("id") Long id,Model model){
//
//        TaskDTO task = taskService.findById(id);
//        List<TaskDTO> tasks = taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
//
//
//        model.addAttribute("task",task);
//        model.addAttribute("users",userService.listAllByRole("employee"));
//        model.addAttribute("projects",projectService.listAllNonCompletedProjects());
//        model.addAttribute("tasks",tasks);
//        model.addAttribute("statuses",Status.values());
//
//        return "task/employee-update";
//
//    }
//
//    @PostMapping("/employee/update/{id}")
//    public String employee_update(@PathVariable("id") Long id,TaskDTO taskDTO){
//        taskService.updateStatus(taskDTO);
//        return "redirect:/task/employee";
//    }
//
//    @GetMapping("/employee/archive")
//    public String employee_archieve(Model model){
//
//        List<TaskDTO> tasks = taskService.listAllTasksByStatus(Status.COMPLETE);
//        model.addAttribute("tasks",tasks);
//        return "task/employee-archive";
//    }


}
