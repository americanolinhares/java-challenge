package com.aubay.challenge.backend.entity;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(@NotBlank String username, String password) {

}
