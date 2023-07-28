package com.aubay.challenge.backend.service.movieapi;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.aubay.challenge.backend.entity.ExternalApiMovieResponse;
import com.aubay.challenge.backend.entity.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;

@Primary
@Service
public class ImdbBotApi extends MovieApiTemplate {

  @Autowired
  ObjectMapper objectMapper;

  @Value("${app.alternativeApiUrl}")
  String movieApiUrl;

  @Override
  public List<Movie> getExternalMovies() throws Exception {

    String externalApiContent = WebClient.create().post().uri(new URI(movieApiUrl)).accept(
        MediaType.APPLICATION_JSON).retrieve()
        .bodyToMono(String.class).block();

    return objectMapper.readValue(externalApiContent, ExternalApiMovieResponse.class).getResults();
  }

}
