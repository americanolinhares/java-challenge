package com.aubay.challenge.backend.controller;

import java.net.URI;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.entity.MovieDTO;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.entity.UserDTO;
import com.aubay.challenge.backend.entity.requests.MovieRequest;
import com.aubay.challenge.backend.entity.requests.RoleRequest;
import com.aubay.challenge.backend.exception.ResourceNotFoundException;
import com.aubay.challenge.backend.exception.UserAlreadyExistsException;
import com.aubay.challenge.backend.service.UserServiceImpl;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

  @Autowired
  UserServiceImpl userServiceImpl;

  @GetMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userServiceImpl.listAll();
    return ResponseEntity.ok(users);
  }
  /*
   * @GetMapping("/userboard")
   * 
   * @PreAuthorize("hasRole('USER')") public String userAccess() { return "User Board."; }
   * 
   * @GetMapping("/admin")
   * 
   * @PreAuthorize("hasRole('ADMIN')") public String adminAccess() { return "Admin Board."; }
   * 
   * @GetMapping("/currentusername")
   * 
   * @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") public String currentUserName() {
   * 
   * Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
   * 
   * if (!(authentication instanceof AnonymousAuthenticationToken)) { return authentication.getName();
   * } else { return "No User"; }
   * 
   * }
   * 
   */

  @PostMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<UserDTO> create(@Valid @RequestBody User user, UriComponentsBuilder uriBuilder)
      throws UserAlreadyExistsException {

    User newUser = userServiceImpl.create(user);
    URI uri = uriBuilder.path("/users/{id}").buildAndExpand(newUser.getId()).toUri();

    return ResponseEntity.created(uri).body(new UserDTO(newUser));
  }

  @PutMapping("/{id}/roles")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDTO> addRole(@PathVariable Long id, @Valid @RequestBody RoleRequest role)
      throws ResourceNotFoundException {

    User user = userServiceImpl.addRole(id, role);
    return ResponseEntity.ok(new UserDTO(user));
  }

  @PatchMapping("/{id}/movies")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<MovieDTO> addMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest movieRequest)
      throws ResourceNotFoundException {

    Movie movie = userServiceImpl.addMovie(id, movieRequest);
    return ResponseEntity.accepted().body(new MovieDTO(movie));
  }

  @DeleteMapping("/{id}/movies")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity removeMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest movie)
      throws ResourceNotFoundException {

    userServiceImpl.removeMovie(id, movie);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/favorite-movies")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<Set<Movie>> favoriteMovies() throws ResourceNotFoundException {

    return new ResponseEntity<Set<Movie>>(userServiceImpl.listFavoriteMovies(), HttpStatus.ACCEPTED);
  }
}
