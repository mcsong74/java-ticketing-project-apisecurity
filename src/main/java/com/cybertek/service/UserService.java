package com.cybertek.service;

import com.cybertek.dto.UserDTO;
import com.cybertek.entity.User;
import com.cybertek.exception.TicketingProjectException;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {

    List<UserDTO> listAllUsers(); //service communicate with DTO, and DTO communicate with Entity
    UserDTO findByUserName(String userName) throws AccessDeniedException;
    UserDTO save(UserDTO dto) throws TicketingProjectException;
    UserDTO update(UserDTO dto) throws TicketingProjectException, AccessDeniedException;
    void delete(String username) throws TicketingProjectException;

    void deleteByUserName(String username);

    List<UserDTO> listAllByRole(String role);

    Boolean checkIfUserCanBeDeleted(User user);

    UserDTO confirm(User user);

}
