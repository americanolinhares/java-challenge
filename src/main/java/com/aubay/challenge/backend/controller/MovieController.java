package com.aubay.challenge.backend.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MovieController {

  @Autowired
  MovieService movieService;

  @GetMapping("/populate")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Populate Movies Database", tags = {"movies"})
  public ResponseEntity<List<Movie>> populateMovies() throws URISyntaxException, IOException {

    List<Movie> movies = movieService.populateDatabase();
    return ResponseEntity.ok(movies);
  }

  @GetMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  @Operation(summary = "List all Movies", tags = {"movies"})
  public ResponseEntity<List<Movie>> getAllMovies() {

    return ResponseEntity.ok(movieService.list());
  }

  @GetMapping("/top")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "List top 10 favorite movies", tags = {"movies"})
  public ResponseEntity<List<Movie>> getTopFavoriteMovies() {

    return ResponseEntity.ok(movieService.topTen());
  }
}

