package com.aubay.challenge.backend.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.entity.requests.MovieRequest;
import com.aubay.challenge.backend.exception.ResourceNotFoundException;
import com.aubay.challenge.backend.repository.MovieRepository;
import com.aubay.challenge.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

  @Mock
  private MovieRepository movieRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private MovieService movieService;


  @Test
  void addMovie_ReturnsCorrectly() throws ResourceNotFoundException {
    // Given
    Movie movie = new Movie("Barbie");
    User user = new User();
    setUser(1L);

    when(movieRepository.findByOriginalTitle("Barbie")).thenReturn(Optional.of(movie));
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    // When
    Movie addedMovie = movieService.addMovie(new MovieRequest("Barbie"));

    // Then
    assertEquals(movie, addedMovie);
    assertEquals(1, addedMovie.getStarNumber());
    assertTrue(user.getFavoriteMovies().contains(movie));
  }

  @Test
  void removeMovie_ReturnsCorrectly() throws ResourceNotFoundException {
    // Given
    User user = new User();
    Movie movie = new Movie("Barbie");
    user.addMovie(movie);
    setUser(1L);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(movieRepository.findByOriginalTitle("Barbie")).thenReturn(Optional.of(movie));

    // When
    movieService.removeMovie("Barbie");

    // Then
    assertFalse(user.getFavoriteMovies().contains(movie));
  }

  @Test
  void removeMovie_ReturnsNotFound() throws ResourceNotFoundException {
    // Given
    User user = new User();

    setUser(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(movieRepository.findByOriginalTitle("Barbie")).thenReturn(Optional.empty());

    // Then
    assertThrows(ResourceNotFoundException.class, () -> movieService.removeMovie("Barbie"));
    assertFalse(user.getFavoriteMovies().contains(new Movie("Barbie")));
  }

  @Test
  void listFavoriteMovies_ReturnsSuccess() throws ResourceNotFoundException {
    // Given
    User user = new User();
    Movie barbie = new Movie("Barbie");
    user.setFavoriteMovies(new HashSet<>(Arrays.asList(barbie, new Movie("ET"), new Movie("Titanic"))));
    setUser(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    // When
    Set<Movie> movies = movieService.listFavoriteMovies();

    // Then
    assertEquals(user.getFavoriteMovies(), movies);
    assertTrue(user.getFavoriteMovies().contains(barbie));
  }

  @Test
  void listMovies_ReturnsCorrectly() {
    // Given
    List<Movie> movies = List.of(new Movie("Matrix 1"), new Movie("Matrix 2"));
    when(movieRepository.findAll()).thenReturn(movies);

    // When
    List<Movie> result = movieService.listMovies();

    // Then
    assertEquals(movies, result);
  }

  @Test
  void listTopTenMovies_ReturnsCorrectly() {
    // Given
    List<Movie> movies =
        IntStream.rangeClosed(1, 10).mapToObj(i -> new Movie("Matrix " + i, i)).collect(Collectors.toList());

    when(movieRepository.findTop10ByOrderByStarNumberDesc()).thenReturn(movies);

    // When
    List<Movie> result = movieService.listTopTenMovies();

    // Then
    assertEquals(movies, result);
  }

  private void setUser(Long userId) {
    Authentication auth = mock(Authentication.class);
    UserDetailsImpl userDetails = new UserDetailsImpl(userId, "username", "password", null);
    when(auth.getPrincipal()).thenReturn(userDetails);
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}
