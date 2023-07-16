package com.aubay.challenge.backend.entity.requests;

import jakarta.validation.constraints.NotEmpty;

public record MovieRequest(@NotEmpty(message = "The title is required.") String title) {

}
