package com.aubay.challenge.backend.entity.requests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserRequestTest {

  @Test
  void setUsername_ValidUsername_Success() {

    // Arrange
    UserRequest authRequest = new UserRequest();
    String username = "john.doe";

    // Act
    authRequest.setUsername(username);

    // Assert
    Assertions.assertEquals(username, authRequest.getUsername());
  }

  @Test
  void setPassword_ValidPassword_Success() {

    // Arrange
    UserRequest authRequest = new UserRequest();
    String password = "pass123";

    // Act
    authRequest.setPassword(password);

    // Assert
    Assertions.assertEquals(password, authRequest.getPassword());
  }
}
