package com.aubay.challenge.backend.service.movieapi;

import java.util.List;
import com.aubay.challenge.backend.entity.Movie;

public abstract class MovieApiTemplate {

  public abstract List<Movie> getExternalMovies() throws Exception;

  public abstract String getApiCode();

}
