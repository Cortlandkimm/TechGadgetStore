package com.techgadget.controller;

import com.techgadget.model.User;
import com.techgadget.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve users", e);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(@PathVariable int id) {
        try {
            return userService.getUserById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve user", e);
        }
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserByUsername(@PathVariable String username) {
        try {
            return userService.getUserByUsername(username);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve user", e);
        }
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User newUser) {
        try {
            return userService.createUser(newUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create user", e);
        }
    }
}

