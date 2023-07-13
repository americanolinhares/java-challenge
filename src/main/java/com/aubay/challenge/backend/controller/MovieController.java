package com.aubay.challenge.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.repository.MovieRepository;
import com.aubay.challenge.backend.service.MovieService;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MovieController {

  private static final String TOKEN =
      "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxYzljY2NkMWFiNzBlZmQyODhiMTE2YmRjZjMwODJmMiIsInN1YiI6IjY0YWMyZjExM2UyZWM4MDBhZjdlODlmZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.xQqLJKM37uZP1FY78YR2Gdso2yfC5IsUfNQOSU0CoOo";

  private static final String MOVIE_API_URL = "https://api.themoviedb.org/3/movie/now_playing";

  @Autowired
  MovieService movieService;

  @Autowired
  MovieRepository movieRepository;


  @GetMapping("")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() throws URISyntaxException, IOException {

    WebClient client = WebClient.create();

    String externalApiContent = client.post().uri(new URI(MOVIE_API_URL))
        .header("Authorization", TOKEN).contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();

    movieService.insertMoviesFromJson(externalApiContent);

    return externalApiContent;
  }

  @GetMapping("/list")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Movie>> getAllMovies() {

    List<Movie> movies = movieRepository.findAll();
    return ResponseEntity.ok(movies);
  }

  @GetMapping("/top")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Movie>> getTopFavoriteMovies() {

    List<Movie> movies = movieRepository.findTop3ByOrderByStarNumberDesc();
    return ResponseEntity.ok(movies);
  }



}

