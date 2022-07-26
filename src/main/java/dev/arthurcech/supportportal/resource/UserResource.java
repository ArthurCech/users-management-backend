package dev.arthurcech.supportportal.resource;

import dev.arthurcech.supportportal.domain.User;
import dev.arthurcech.supportportal.exception.domain.EmailExistException;
import dev.arthurcech.supportportal.exception.domain.UserNotFoundException;
import dev.arthurcech.supportportal.exception.domain.UsernameExistException;
import dev.arthurcech.supportportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user)
            throws UserNotFoundException, EmailExistException, UsernameExistException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getEmail());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

}
