package com.aubay.challenge.backend.entity;

import java.util.Set;

public record UserDTO(Long id, String username, Set<Role> roles, Set<Movie> favoriteMovies) {

  public UserDTO(User user) {
    this(user.getId(), user.getUsername(), user.getRoles(), user.getFavoriteMovies());
  }
}
