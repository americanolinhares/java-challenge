package com.aubay.challenge.backend.service.movieapi;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.aubay.challenge.backend.entity.Movie;

@Component
public class MovieExternalApiService {

  private final Map<String, MovieApiTemplate> apisByCode;

  public MovieExternalApiService(List<MovieApiTemplate> apis) {
    apisByCode = apis.stream()
        .collect(Collectors.toMap(MovieApiTemplate::getApiCode, Function.identity()));
  }

  public List<Movie> getExternalMovies(String apiCode) throws Exception {
    MovieApiTemplate service = apisByCode.get(apiCode);
    return service.getExternalMovies();
  }
}
