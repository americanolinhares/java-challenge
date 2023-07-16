package com.aubay.challenge.backend.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.aubay.challenge.backend.entity.ExternalApiMovieResponse;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MovieService {

  @Autowired
  MovieRepository movieRepository;

  @Autowired
  ObjectMapper objectMapper;

  @Value("${aubay.movieApiToken}")
  private String token;

  @Value("${aubay.movieApiUrl}")
  private String movieApiUrl;

  public List<Movie> populateDatabase() throws IOException, URISyntaxException {

    String externalApiContent = WebClient.create().post().uri(new URI(movieApiUrl)).header("Authorization", token)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON).retrieve()
        .bodyToMono(String.class).block();

    List<Movie> movies = objectMapper.readValue(externalApiContent, ExternalApiMovieResponse.class).getResults();
    return movieRepository.saveAll(movies);
  }

  public List<Movie> list() {
    return movieRepository.findAll();
  }

  public List<Movie> topTen() {
    return movieRepository.findTop10ByOrderByStarNumberDesc();
  }
}
