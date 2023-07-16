package com.aubay.challenge.backend.entity.requests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AuthRequestTest {

  @Test
  void setUsername_ValidUsername_Success() {

    // Arrange
    AuthRequest authRequest = new AuthRequest();
    String username = "john.doe";

    // Act
    authRequest.setUsername(username);

    // Assert
    Assertions.assertEquals(username, authRequest.getUsername());
  }

  @Test
  void setPassword_ValidPassword_Success() {

    // Arrange
    AuthRequest authRequest = new AuthRequest();
    String password = "pass123";

    // Act
    authRequest.setPassword(password);

    // Assert
    Assertions.assertEquals(password, authRequest.getPassword());
  }
}
