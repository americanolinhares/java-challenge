package com.aubay.challenge.backend.entity;

public record MovieDTO(Long id, String originalTitle, int starNumber) {

  public MovieDTO(Movie movie) {
    this(movie.getId(), movie.getOriginalTitle(), movie.getStarNumber());
  }
}


