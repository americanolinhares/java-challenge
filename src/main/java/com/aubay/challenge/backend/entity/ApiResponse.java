package com.aubay.challenge.backend.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse {

  private List<Movie> results;

  @JsonProperty("results")
  public List<Movie> getResults() {
    return results;
  }

  @JsonProperty("results")
  public void setResults(List<Movie> results) {
    this.results = results;
  }
}
