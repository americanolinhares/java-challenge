package com.aubay.challenge.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.entity.Role;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.entity.requests.MovieRequest;
import com.aubay.challenge.backend.repository.MovieRepository;
import com.aubay.challenge.backend.security.JwtUtils;
import com.aubay.challenge.backend.service.MovieService;
import com.aubay.challenge.backend.service.UserDetailsImpl;
import com.aubay.challenge.backend.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(classes = AubayApplication.class)
@ActiveProfiles(value = "dev")
class FavoriteMovieControllerIntegrationTest {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private MockMvc mockMvc;

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
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }


  @WithMockUser
  @Test
  void addFavoriteMovie_ReturnsCreated() throws Exception {

    mockAuth();
    MovieRequest movieRequest = new MovieRequest("Elemental");
    Movie elemental = new Movie("Elemental");

    when(movieService.addFavoriteMovie(movieRequest)).thenReturn(elemental);
    mockMvc.perform(
        put("/favorite-movies").contentType("application/json").content(objectMapper.writeValueAsString(movieRequest)))
        .andExpect(status().isCreated());
  }

  private void mockAuth() {
    when(jwtUtils.validateJwtToken(Mockito.any(String.class))).thenReturn(true);
    when(jwtUtils.getUserNameFromJwtToken(Mockito.any(String.class))).thenReturn("kelly");
    User user = new User();
    user.addRole(new Role("user"));
    when(userDetailsService.loadUserByUsername("kelly")).thenReturn(UserDetailsImpl.build(user));
}

}
