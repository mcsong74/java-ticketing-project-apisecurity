package com.cybertek.controller;

import com.cybertek.annotation.DefaultExceptionMessage;
//import com.cybertek.converter.UserDtoConverter;
import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.Project;
import com.cybertek.entity.ResponseWrapper;
import com.cybertek.enums.Status;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.service.ProjectService;
import com.cybertek.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/project")
@Tag(name = "Project Controller", description = "Project API")
public class ProjectController {


    private ProjectService projectService;
    private UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Read all projects")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong getting all projects, try again!")
    @PreAuthorize("hasAnyAuthority('Admin',  'Manager')")
    public ResponseEntity<ResponseWrapper> readAll(){
        List<ProjectDTO> projectDTOList = projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("All projects are retrieved", projectDTOList));
    }

    @GetMapping("/{projectcode}")
    @Operation(summary = "Read projects by project code")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong getting project by project code, try again!")
    @PreAuthorize("hasAnyAuthority('Admin',  'Manager')")
    public ResponseEntity<ResponseWrapper> readByProject(@PathVariable("projectcode") String projectcode){
        ProjectDTO projectDTO = projectService.getByProjectCode(projectcode);
        return ResponseEntity.ok(new ResponseWrapper("Project is retrieved", projectDTO));
    }

    @PostMapping
    @Operation(summary = "Create a project")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong creating project, try again!")
    @PreAuthorize("hasAnyAuthority('Admin',  'Manager')")
    public ResponseEntity<ResponseWrapper> create(@RequestBody ProjectDTO projectDTO) throws TicketingProjectException {

        projectService.save(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project is retrieved", projectDTO));
    }

    @PutMapping
    @Operation(summary = "Update a project")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong Updating project, try again!")
    @PreAuthorize("hasAnyAuthority('Admin',  'Manager')")
    public ResponseEntity<ResponseWrapper> update(@RequestBody ProjectDTO projectDTO) throws TicketingProjectException {
        ProjectDTO updatedProject = projectService.update(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project is updated", updatedProject));
    }

    @DeleteMapping("/{projectcode}")
    @Operation(summary = "Delete a project")
    @DefaultExceptionMessage(defaultMessage = "Failed to delete the project, try again!")
    @PreAuthorize("hasAnyAuthority('Admin',  'Manager')")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable("projectcode") String projectcode) throws TicketingProjectException {
         projectService.delete(projectcode);
        return ResponseEntity.ok(new ResponseWrapper("Project is deleted"));
    }

    @PutMapping("/complete/{projectcode}")
    @Operation(summary = "Complete a project by project code")
    @DefaultExceptionMessage(defaultMessage = "Failed to Complete the project, try again!")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> complete(@PathVariable("projectcode") String projectcode) throws TicketingProjectException {
        ProjectDTO projectDTO= projectService.complete(projectcode);
        return ResponseEntity.ok(new ResponseWrapper("Project is completed", projectDTO));
    }

    @GetMapping("/details")
    @Operation(summary = "Read all project details")
    @DefaultExceptionMessage(defaultMessage = "Failed to read all the project details, try again!")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> complete() throws TicketingProjectException {
        List<ProjectDTO> projectDTOList= projectService.listAllProjectDetails();
        return ResponseEntity.ok(new ResponseWrapper("Projects are completed", projectDTOList));
    }

//    @GetMapping("/create")
//    public String createProject(Model model){
//
//        model.addAttribute("project",new ProjectDTO());
//        model.addAttribute("projects",projectService.listAllProjects());
//        model.addAttribute("managers",userService.listAllByRole("manager"));
//
//        return "/project/create";
//    }
//
//    @PostMapping("/create")
//    public String insertProject(ProjectDTO project){
//        projectService.save(project);
//        return "redirect:/project/create";
//
//    }
//
//    @GetMapping("/delete/{projectcode}")
//    public String deleteProject(@PathVariable("projectcode") String projectcode){
//
//        projectService.delete(projectcode);
//        return "redirect:/project/create";
//    }
//
//
//    @GetMapping("/complete/{projectcode}")
//    public String completeProject(@PathVariable("projectcode") String projectcode){
//        projectService.complete(projectcode);
//        return "redirect:/project/create";
//    }
//
//
//    @GetMapping("/update/{projectcode}")
//    public String editProject(@PathVariable("projectcode") String projectcode,Model model){
//
//        model.addAttribute("project",projectService.getByProjectCode(projectcode));
//        model.addAttribute("projects",projectService.listAllProjects());
//        model.addAttribute("managers",userService.listAllByRole("manager"));
//
//        return "/project/update";
//    }
//
//    @PostMapping("/update/{projectcode}")
//    public String updateProject(@PathVariable("projectcode") String projectcode,ProjectDTO project){
//
//        projectService.update(project);
//
//        return "redirect:/project/create";
//    }
//
//
//    @GetMapping("/manager/complete")
//    public String getProjectByManager(Model model){
//
//        List<ProjectDTO> projects = projectService.listAllProjectDetails();
//        model.addAttribute("projects",projects);
//
//        return "/manager/project-status";
//    }
//
//    @GetMapping("/manager/complete/{projectCode}")
//    public String manager_completed(@PathVariable("projectCode") String projectCode,Model model){
//
//        projectService.complete(projectCode);
//
//        return "redirect:/project/manager/complete";
//    }


}





















