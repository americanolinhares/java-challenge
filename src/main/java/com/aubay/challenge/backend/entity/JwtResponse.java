package com.aubay.challenge.backend.entity;

import java.util.List;

public record JwtResponse(String accessToken, String tokenType, Long id, String username,
    List<String> roles) {

  public JwtResponse(String accessToken, Long id, String username, List<String> roles) {

    this(accessToken, "Bearer", id, username, roles);
  }
}
