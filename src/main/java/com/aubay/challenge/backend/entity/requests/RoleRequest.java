package com.aubay.challenge.backend.entity.requests;

import jakarta.validation.constraints.NotEmpty;

public record RoleRequest(@NotEmpty(message = "The role is required.") String name) {
}
