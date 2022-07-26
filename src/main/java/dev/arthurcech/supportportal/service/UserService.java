package dev.arthurcech.supportportal.service;

import dev.arthurcech.supportportal.domain.User;
import dev.arthurcech.supportportal.exception.domain.EmailExistException;
import dev.arthurcech.supportportal.exception.domain.UserNotFoundException;
import dev.arthurcech.supportportal.exception.domain.UsernameExistException;

import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

}
