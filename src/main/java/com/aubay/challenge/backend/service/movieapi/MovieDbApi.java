package com.aubay.challenge.backend.service.movieapi;

import java.net.URI;
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
public class MovieDbApi extends MovieApiTemplate {

  @Autowired
  ObjectMapper objectMapper;

  @Value("${app.mainApiUrl}")
  private String movieApiUrl;

  @Value("${app.mainApiToken}")
  private String token;

  @Override
  public List<Movie> getExternalMovies() throws Exception {

    String externalApiContent = WebClient.create().post().uri(new URI(movieApiUrl))
        .header("Authorization",
            token).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();

    return objectMapper.readValue(externalApiContent, ExternalApiMovieResponse.class).getResults();
  }

  /*
   * @Override public List<Movie> retriveExternalMovies() throws IOException, URISyntaxException {
   * 
   * MovieExternalApi externalApi = new MovieDbApi();
   * 
   * WebClient client = externalApi.createWebClient();
   * 
   * String externalApiContent = client.post().uri(new URI(movieApiUrl)).header("Authorization",
   * token).accept( MediaType.APPLICATION_JSON).retrieve() .bodyToMono(String.class).block();
   * 
   * return objectMapper.readValue(externalApiContent, ExternalApiMovieResponse.class).getResults(); }
   */

}
