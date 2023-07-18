package com.aubay.challenge.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.entity.UserDTO;
import com.aubay.challenge.backend.entity.requests.RoleRequest;
import com.aubay.challenge.backend.entity.requests.UserRequest;
import com.aubay.challenge.backend.exception.ResourceNotFoundException;
import com.aubay.challenge.backend.exception.UserAlreadyExistsException;
import com.aubay.challenge.backend.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "users", description = "Operations about users")
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

  @Autowired
  UserServiceImpl userServiceImpl;

  @GetMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(summary = "List all users", tags = {"users"})
  @SecurityRequirement(name = "Bearer Authentication")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userServiceImpl.listAll();
    return ResponseEntity.ok(users);
  }

  @PutMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(summary = "Create an user", tags = {"users"})
  @SecurityRequirement(name = "Bearer Authentication")
  public ResponseEntity<UserDTO> create(@Valid @RequestBody UserRequest userRequest, UriComponentsBuilder uriBuilder)
      throws UserAlreadyExistsException, ResourceNotFoundException {

    User newUser = userServiceImpl.create(new User(userRequest.getUsername(), userRequest.getPassword()));

    return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(newUser));
  }

  @PutMapping("/{id}/roles")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Add a role to an user", tags = {"users"})
  @SecurityRequirement(name = "Bearer Authentication")
  public ResponseEntity<UserDTO> addRole(@PathVariable Long id, @Valid @RequestBody RoleRequest role)
      throws ResourceNotFoundException {

    User user = userServiceImpl.addRole(id, role);
    return ResponseEntity.ok(new UserDTO(user));
  }
}
