package com.aubay.challenge.backend.controller;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.entity.NewRole;
import com.aubay.challenge.backend.entity.RequestMovie;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.service.UserServiceImpl;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

  @Autowired
  UserServiceImpl userServiceImpl;

  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userServiceImpl.listAll();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/userboard")
  @PreAuthorize("hasRole('USER')")
  public String userAccess() {
    return "User Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> createUser(@RequestBody User user) {

    User usr = userServiceImpl.registerDefaultUser(user);
    return new ResponseEntity<>(usr, HttpStatus.CREATED);
  }

  @PutMapping("/{id}/roles")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> editUser(@PathVariable Long id, @RequestBody NewRole role) {

    User usr = userServiceImpl.edit(id, role);
    return new ResponseEntity<>(usr, HttpStatus.ACCEPTED);
  }

  @PatchMapping("/{id}/movies")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity addMovie(@PathVariable Long id, @RequestBody RequestMovie movie) {
    userServiceImpl.addMovie(id, movie);
    return new ResponseEntity<>(movie, HttpStatus.ACCEPTED);
  }

  @DeleteMapping("/{id}/movies")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity deleteMovie(@PathVariable Long id, @RequestBody RequestMovie movie) {
    userServiceImpl.removeMovie(id, movie);
    return new ResponseEntity<>(movie, HttpStatus.ACCEPTED);
  }

  @GetMapping("/favorite-movies")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<Set<Movie>> favoriteMovies() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = "";
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      username = authentication.getName();
    } else {
      System.out.println("No User");
    }
    Set<Movie> movies = userServiceImpl.listFavoriteMovies(username);
    return new ResponseEntity<Set<Movie>>(movies, HttpStatus.ACCEPTED);
  }



  @GetMapping("/currentusername")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public String currentUserName() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      return authentication.getName();
    } else {
      return "No User";
    }

  }

}
