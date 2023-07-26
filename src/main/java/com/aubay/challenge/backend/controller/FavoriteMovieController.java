package com.aubay.challenge.backend.controller;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.entity.MovieDTO;
import com.aubay.challenge.backend.entity.requests.MovieRequest;
import com.aubay.challenge.backend.exception.ResourceNotFoundException;
import com.aubay.challenge.backend.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "favorite-movies", description = "Operations about favorite movies")
@RestController
@RequestMapping("/favorite-movies")
@CrossOrigin(origins = "*", maxAge = 3600)
@SecurityRequirement(name = "Bearer Authentication")
public class FavoriteMovieController {

  @Autowired
  MovieService movieService;

  @PutMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(summary = "Add a movie to the favorite movie list", tags = {"favorite-movies"})
  public ResponseEntity<MovieDTO> addMovie(@Valid @RequestBody MovieRequest movieRequest) {

    Movie movie = movieService.addFavoriteMovie(movieRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(new MovieDTO(movie));
  }

  @DeleteMapping("/{movieTitle}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(summary = "Remove a movie from the favorite movie list", tags = {"favorite-movies"})
  public ResponseEntity<Object> removeMovie(@PathVariable String movieTitle) throws ResourceNotFoundException {

    movieService.removeFavoriteMovie(movieTitle);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(summary = "List favorite movies", tags = {"favorite-movies"})
  public ResponseEntity<Set<Movie>> favoriteMovies() throws ResourceNotFoundException {

    return ResponseEntity.accepted().body(movieService.listFavoriteMovies());
  }
}

