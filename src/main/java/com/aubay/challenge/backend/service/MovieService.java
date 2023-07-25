package com.aubay.challenge.backend.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import com.aubay.challenge.backend.entity.ExternalApiMovieResponse;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.entity.requests.MovieRequest;
import com.aubay.challenge.backend.exception.ResourceNotFoundException;
import com.aubay.challenge.backend.repository.MovieRepository;
import com.aubay.challenge.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class MovieService {

  private static final Logger logger = LoggerFactory.getLogger(MovieService.class);


  @Autowired
  MovieRepository movieRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  private HazelcastInstance hazelcastInstance;

  private List<Movie> topMoviesCache;

  @Value("${app.movieApiToken}")
  private String token;

  @Value("${app.movieApiUrl}")
  private String movieApiUrl;

  private static final String MOVIE_NOT_FOUND = "Movie not found";
  private static final String USER_NOT_FOUND = "User not found";


  @PostConstruct
  public void init() {
    topMoviesCache = Collections.emptyList();
  }

  public Movie addMovie(MovieRequest newMovie) throws ResourceNotFoundException {

    UserDetailsImpl userDetails = extractUserPrincipal();
    User user = extractUser(userDetails.getId());
    Movie movie = extractMovie(newMovie.title());

    movie.addStarNumber();
    user.addMovie(movie);
    userRepository.save(user);

    return movie;
  }

  public User removeMovie(String movieTitle) throws ResourceNotFoundException {

    UserDetailsImpl userDetails = extractUserPrincipal();
    User user = extractUser(userDetails.getId());
    Movie movie = extractMovie(movieTitle);

    if (!user.getFavoriteMovies().contains(movie)) {
      throw new ResourceNotFoundException("That movie does not belong to the user's favorite list.");
    }

    movie.subtractStarNumber();
    user.removeMovie(movie);
    return userRepository.save(user);
  }

  public Set<Movie> listFavoriteMovies() throws ResourceNotFoundException {

    return extractUser(extractUserPrincipal().getId()).getFavoriteMovies();
  }


  public List<Movie> populateDatabase() throws IOException, URISyntaxException {

    String externalApiContent = WebClient.create().post().uri(new URI(movieApiUrl)).header("Authorization", token)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON).retrieve()
        .bodyToMono(String.class).block();

    List<Movie> movies = objectMapper.readValue(externalApiContent, ExternalApiMovieResponse.class).getResults();
    return movieRepository.saveAll(movies);
  }

  public List<Movie> listMovies() {
    return movieRepository.findAll();
  }

  /*
   * @Cacheable("movies") public List<Movie> listTopTenMovies() {
   * 
   * return movieRepository.findTop10ByOrderByStarNumberDesc(); }
   */

  @Cacheable("movies")
  @RateLimiter(name = "topMoviesRateLimiter", fallbackMethod = "getTopMoviesFallback")
  public List<Movie> listTopTenMoviesWithRateLimit() {

    logger.warn(
        "------------------------------------------------------listTopTenMoviesWithRateLimit()------------------------------------------------------------------------");


    return movieRepository.findTop10ByOrderByStarNumberDesc();
  }

  public List<Movie> getTopMoviesFallback(Exception e) {

    logger.warn(
        "------------------------------------------------------getTopMoviesFallback()------------------------------------------------------------------------");
    if (!topMoviesCache.isEmpty()) {
      return topMoviesCache;
    }

    // Alternatively, you can return an empty list or any other fallback behavior
    return Collections.emptyList();
  }


  private UserDetailsImpl extractUserPrincipal() throws ResourceNotFoundException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new ResourceNotFoundException(USER_NOT_FOUND);
    }
    return (UserDetailsImpl) authentication.getPrincipal();
  }

  private User extractUser(Long id) throws ResourceNotFoundException {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isEmpty()) {
      throw new ResourceNotFoundException(USER_NOT_FOUND);
    }
    return optionalUser.get();
  }

  private Movie extractMovie(String movieTitle) throws ResourceNotFoundException {
    Optional<Movie> optionalMovie = movieRepository.findByOriginalTitle(movieTitle);
    if (optionalMovie.isEmpty()) {
      throw new ResourceNotFoundException(MOVIE_NOT_FOUND);
    }
    return optionalMovie.get();
  }
}
