package com.aubay.challenge.backend.service;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.entity.ApiResponse;
import com.aubay.challenge.backend.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MovieService {

  private final MovieRepository movieRepository;
  private final ObjectMapper objectMapper;

  public MovieService(MovieRepository movieRepository, ObjectMapper objectMapper) {

    this.movieRepository = movieRepository;
    this.objectMapper = objectMapper;
  }

  public void insertMoviesFromJson(String moviesJson) throws IOException {

    ApiResponse movieList = objectMapper.readValue(moviesJson, ApiResponse.class);
    List<Movie> movies = movieList.getResults();
    System.out.println(movies);
    movieRepository.saveAll(movies);
  }
}
