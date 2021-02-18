package com.cybertek.controller;

import com.cybertek.annotation.DefaultExceptionMessage;
import com.cybertek.dto.MailDTO;
import com.cybertek.dto.RoleDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.ConfirmationToken;
import com.cybertek.entity.ResponseWrapper;
import com.cybertek.entity.User;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.mapper.MapperUtil;
import com.cybertek.service.ConfirmationTokenService;
import com.cybertek.service.RoleService;
import com.cybertek.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Controller", description = "User API")
public class UserController {
    @Value("${app.local-url}")
    private String BASE_URL;

    private UserService userService;
    private MapperUtil mapperUtil;
    private RoleService roleService;
    private ConfirmationTokenService confirmationTokenService;

    public UserController(UserService userService, MapperUtil mapperUtil, RoleService roleService, ConfirmationTokenService confirmationTokenService) {
        this.userService = userService;
        this.mapperUtil = mapperUtil;
        this.roleService = roleService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @DefaultExceptionMessage(defaultMessage = "Something went wrong in email confirmation, try again!")
    @PostMapping("/create-user")
    @Operation(summary = "Create new account")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ResponseWrapper> doRegister(@RequestBody UserDTO userDTO) throws TicketingProjectException {

        UserDTO createUser =userService.save(userDTO);

        sendEmail(createEmail(createUser));

        return ResponseEntity.ok(new ResponseWrapper("User has been created", createUser));

    }

    @GetMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong in retrieving all users, try again!")
    @Operation(summary = "Read All Users")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ResponseWrapper> readAll(){
        //business logic - data (model)
        List<UserDTO> result = userService.listAllUsers();
        //bind it to view
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved users!", result));

    }

    @GetMapping("/{username}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    @Operation(summary = "Read User")
    // TODO : only admin should see other profiles or current user can see his/her profile
    public ResponseEntity<ResponseWrapper> readByUserName(@PathVariable("username") String username){
        UserDTO user = userService.findByUserName(username);
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved user!", user));

    }

    @PutMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong updating user profile, try again!")
    @Operation(summary = "Update User")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user) throws TicketingProjectException {
        UserDTO updatedUser = userService.update(user);
        return ResponseEntity.ok(new ResponseWrapper("Successfully user updated!", updatedUser));
    }

    @DeleteMapping("/{username}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong deleting user profile, try again!")
    @Operation(summary = "Delete User")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username) throws TicketingProjectException {
        userService.delete(username);
        return ResponseEntity.ok(new ResponseWrapper("Successfully user deleted!"));

    }

    @GetMapping("/role")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong read user profile by role, try again!")
    @Operation(summary = "Read Users by Role")
    @PreAuthorize("hasAnyAuthority('Admin', 'Manager')")
    public ResponseEntity<ResponseWrapper> readByRole(@RequestParam String role){
        List<UserDTO> userDTOList = userService.listAllByRole(role);
        return ResponseEntity.ok(new ResponseWrapper("Successfully read users by role ["+ role +"]!", userDTOList));
    }



/*-----------------------------------------------------------------------------------------------------------*/
    private MailDTO createEmail(UserDTO userDTO){
        User user = mapperUtil.convert(userDTO, new User());
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationToken.setIsDeleted(false);
        // save in the database
        ConfirmationToken createdConfirmationToken = confirmationTokenService.save(confirmationToken);
        return MailDTO.builder()
                .emailTo(user.getUserName())
                .token(createdConfirmationToken.getToken())
                .subject("Confirm Registration")
                .message("To confirm your account, please click here: ")
                .url(BASE_URL + "/confirmation?token=")
                .build();
    }

    private void sendEmail(MailDTO mailDTO){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailDTO.getEmailTo());
        mailMessage.setSubject(mailDTO.getSubject());
        mailMessage.setText(mailDTO.getMessage() + mailDTO.getUrl() + mailDTO.getToken());

        confirmationTokenService.sendEmail(mailMessage);

    }








//    @Autowired
//    RoleService roleService;
//    @Autowired
//    UserService userService;
//
//    @GetMapping("/create")
//    public String createUser(Model model){
//        model.addAttribute("user",new UserDTO());
//        model.addAttribute("roles",roleService.listAllRoles());
//        model.addAttribute("users",userService.listAllUsers());
//        return "/user/create";
//    }
//
//    @PostMapping("/create")
//    public String insertUser(UserDTO user,Model model) throws TicketingProjectException {
//        userService.save(user);
//        return "redirect:/user/create";
//    }
//    //
//    @GetMapping("/update/{username}")
//    public String editUser(@PathVariable("username") String username,Model model){
//
//        model.addAttribute("user",userService.findByUserName(username));
//        model.addAttribute("users",userService.listAllUsers());
//        model.addAttribute("roles",roleService.listAllRoles());
//
//        return "/user/update";
//
//    }
//
//    @PostMapping("/update/{username}")
//    public String updateUser(@PathVariable("username") String username,UserDTO user,Model model){
//        userService.update(user);
//        return "redirect:/user/create";
//    }
//
//    @GetMapping("/delete/{username}")
//    public String deleteUser(@PathVariable("username") String username) throws TicketingProjectException {
//        userService.delete(username);
//        return "redirect:/user/create";
//    }

}
