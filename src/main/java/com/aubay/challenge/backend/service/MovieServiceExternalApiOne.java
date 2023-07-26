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
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MovieServiceExternalApiOne {

  @Autowired
  private ObjectMapper objectMapper;

  @Value("${app.movieApiToken}")
  private String token;

  @Value("${app.movieApiUrl}")
  private String movieApiUrl;

  public List<Movie> retriveExternalMovies() throws IOException, URISyntaxException {

    String externalApiContent = WebClient.create().post().uri(new URI(movieApiUrl)).header("Authorization", token)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON).retrieve()
        .bodyToMono(String.class).block();

    return objectMapper.readValue(externalApiContent, ExternalApiMovieResponse.class).getResults();
  }

}
