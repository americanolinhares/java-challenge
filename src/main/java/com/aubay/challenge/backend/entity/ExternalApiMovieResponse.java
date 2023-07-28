package com.aubay.challenge.backend.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalApiMovieResponse {

  @JsonProperty("results")
  @JsonAlias("description")
  private List<Movie> results;

  public List<Movie> getResults() {

    return results;
  }

  public void setResults(List<Movie> results) {

    this.results = results;
  }
}
