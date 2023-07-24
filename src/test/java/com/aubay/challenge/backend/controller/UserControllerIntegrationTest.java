package com.aubay.challenge.backend.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.aubay.challenge.backend.AubayApplication;
import com.aubay.challenge.backend.entity.Role;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.entity.requests.UserRequest;
import com.aubay.challenge.backend.repository.MovieRepository;
import com.aubay.challenge.backend.repository.UserRepository;
import com.aubay.challenge.backend.security.JwtUtils;
import com.aubay.challenge.backend.service.MovieService;
import com.aubay.challenge.backend.service.UserDetailsImpl;
import com.aubay.challenge.backend.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(classes = AubayApplication.class)
@ActiveProfiles(value = "prod")
class UserControllerIntegrationTest {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MovieService movieService;

  @MockBean
  private MovieRepository movieRepository;

  @MockBean
  private UserDetailsServiceImpl userDetailsService;


  @MockBean
  private JwtUtils jwtUtils;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @WithMockUser
  @Test
  void createUser_ReturnsOK() throws Exception {
    // Given
    mockAuth();
    UserRequest userRequest = new UserRequest();
    userRequest.setUsername("maria");
    userRequest.setPassword("12345678");

    // When
    mockMvc.perform(put("/users").contentType("application/json").content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isCreated());

    // Then
    assertTrue(userRepository.findByUsername("maria").isPresent());
  }

  @WithMockUser
  @Test
  void getAllUsers_ReturnsOK() throws Exception {
    // Given
    mockAuth();

    // When
    mockMvc.perform(get("/users")).andDo(print()).andExpect(status().isOk());

    // Then
    Optional<User> userEntity = userRepository.findById(1L);
    assertEquals("fred", userEntity.get().getUsername());
  }

  private void mockAuth() {
    when(jwtUtils.validateJwtToken(Mockito.any(String.class))).thenReturn(true);
    when(jwtUtils.getUserNameFromJwtToken(Mockito.any(String.class))).thenReturn("kelly");
    User user = new User();
    user.addRole(new Role("user"));
    when(userDetailsService.loadUserByUsername("kelly")).thenReturn(UserDetailsImpl.build(user));
}

}
