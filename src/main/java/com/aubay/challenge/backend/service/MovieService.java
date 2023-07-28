package com.aubay.challenge.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.entity.requests.MovieRequest;
import com.aubay.challenge.backend.exception.ResourceNotFoundException;
import com.aubay.challenge.backend.repository.MovieRepository;
import com.aubay.challenge.backend.repository.UserRepository;
import com.aubay.challenge.backend.service.movieapi.ImdbBotApi;
import com.aubay.challenge.backend.service.movieapi.MovieDbApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@Service
@Transactional
public class MovieService {

  @Autowired
  MovieRepository movieRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ImdbBotApi imdbBotApi;

  @Autowired
  MovieDbApi movieDbApi;

  @Autowired
  private HazelcastInstance cache;

  private static final String MOVIE_NOT_FOUND = "Movie not found";
  private static final String USER_NOT_FOUND = "User not found";
  private static final String MOVIES = "movies";
  // private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

  public Movie addFavoriteMovie(MovieRequest newMovie) {

    UserDetailsImpl userDetails = findUserPrincipal();
    User user = findUser(userDetails.getId());
    Movie movie = findMovie(newMovie.title());
    movie.addStarNumber();
    /*
     * 0 star - user 1 0 star - user 2 0 star - user 3 1 star - user 1 1 star - user 2 1 star - user 3
     * TODO: Fix race condition
     */
    user.addMovie(movie);
    userRepository.save(user);
    return movie;
  }

  public User removeFavoriteMovie(String movieTitle) throws ResourceNotFoundException {

    UserDetailsImpl userDetails = findUserPrincipal();
    User user = findUser(userDetails.getId());
    Movie movie = findMovie(movieTitle);

    if (!user.getFavoriteMovies().contains(movie)) {
      throw new ResourceNotFoundException("That movie does not belong to the user's favorite list.");
    }

    movie.subtractStarNumber();
    user.removeMovie(movie);
    return userRepository.save(user);
  }

  public Set<Movie> listFavoriteMovies() throws ResourceNotFoundException {

    return findUser(findUserPrincipal().getId()).getFavoriteMovies();
  }

  @CircuitBreaker(name = "movies", fallbackMethod = "populateDatabaseFallback")
  public List<Movie> populateDatabase() throws Exception {

    return movieRepository.saveAll(imdbBotApi.getExternalMovies());
  }

  public List<Movie> populateDatabaseFallback(RuntimeException e) throws Exception {

    return movieRepository.saveAll(movieDbApi.getExternalMovies());
  }

  public List<Movie> listMovies() {

    return movieRepository.findAll();
  }

  @RateLimiter(name = MOVIES, fallbackMethod = "getTopMoviesFallback")
  public List<Movie> listTopTenMoviesWithRateLimit() {

    List<Movie> movies = movieRepository.findTop10ByOrderByStarNumberDesc();
    retrieveCache().put(MOVIES, movies);
    return movies;
  }

  public List<Movie> getTopMoviesFallback(RequestNotPermitted ex) {
    return (List<Movie>) retrieveCache().get(MOVIES);
  }

  private UserDetailsImpl findUserPrincipal() throws ResourceNotFoundException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new ResourceNotFoundException(USER_NOT_FOUND);
    }
    return (UserDetailsImpl) authentication.getPrincipal();
  }

  private User findUser(Long id) throws ResourceNotFoundException {

    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isEmpty()) {
      throw new ResourceNotFoundException(USER_NOT_FOUND);
    }
    return optionalUser.get();
  }

  private Movie findMovie(String movieTitle) throws ResourceNotFoundException {

    Optional<Movie> optionalMovie = movieRepository.findByOriginalTitle(movieTitle);
    if (optionalMovie.isEmpty()) {
      throw new ResourceNotFoundException(MOVIE_NOT_FOUND);
    }
    return optionalMovie.get();
  }

  private ConcurrentMap<String, Object> retrieveCache() {

    return cache.getMap(MOVIES);
  }
}
