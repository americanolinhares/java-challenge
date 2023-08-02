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
import com.aubay.challenge.backend.service.movieapi.MovieExternalApiService;
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
  private MovieExternalApiService externalApiService;

  @Autowired
  private HazelcastInstance cache;

  @Autowired
  RandomMovies randomMovies;

  private static final String MOVIE_NOT_FOUND = "Movie not found";
  private static final String USER_NOT_FOUND = "User not found";
  private static final String MOVIES = "movies";

  public Movie addFavoriteMovie(MovieRequest newMovie) {

    /*
     * UserDetailsImpl userDetails = findUserPrincipal(); User user = findUser(userDetails.getId());
     * Movie movie = findMovie(newMovie.title()); movie.addStarNumber(); /* 0 star - user 1 0 star -
     * user 2 0 star - user 3 1 star - user 1 1 star - user 2 1 star - user 3 TODO: Fix race condition
     * 
     * user.addMovie(movie); userRepository.save(user);
     */

    UserDetailsImpl userDetails = findUserPrincipal();
    User user = findUser(userDetails.getId());
    Movie movie = findMovie(newMovie.title());

    synchronized (movie) {
      movie.addStarNumber();
    }

    synchronized (user) {
      user.addMovie(movie);
      userRepository.save(user);
    }

    return movie;
  }

  public User removeFavoriteMovie(String movieTitle) throws ResourceNotFoundException {

    UserDetailsImpl userDetails = findUserPrincipal();
    User user = findUser(userDetails.getId());
    Movie movie = findMovie(movieTitle);

    if (!user.getFavoriteMovies().contains(movie)) {
      throw new ResourceNotFoundException("That movie does not belong to the user's favorite list.");
    }

    synchronized (movie) {
      movie.subtractStarNumber();
    }

    synchronized (user) {
      user.removeMovie(movie);
      userRepository.save(user);
    }
    return user;
  }

  public Set<Movie> listFavoriteMovies() throws ResourceNotFoundException {

    return findUser(findUserPrincipal().getId()).getFavoriteMovies();
  }

  @CircuitBreaker(name = "movies", fallbackMethod = "populateDatabaseFallback")
  public List<Movie> populateDatabase() throws Exception {

    return movieRepository.saveAll(externalApiService.getExternalMovies("1"));
  }

  public List<Movie> populateDatabaseFallback(RuntimeException e) throws Exception {

    return movieRepository.saveAll(externalApiService.getExternalMovies("2"));
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

  public Movie addRandomMovie() {

    Optional<Movie> randomMovie = movieRepository.findByOriginalTitle(randomMovies.getNewSuggestedMovie());
    if (randomMovie.isPresent()) {
      UserDetailsImpl userDetails = findUserPrincipal();
      User user = findUser(userDetails.getId());
      Movie movie = findMovie(randomMovies.getNewSuggestedMovie());
      movie.addStarNumber();
      user.addMovie(movie);
      userRepository.save(user);
      return movie;
    }
    return findAleatoryMovie();
  }

  private Movie findAleatoryMovie() {
    UserDetailsImpl userDetails = findUserPrincipal();
    User user = findUser(userDetails.getId());

    List<Movie> allMovies = movieRepository.findAll();
    Set<Movie> favoriteMovies = user.getFavoriteMovies();

    List<Movie> nonFavoriteMovies = allMovies.stream()
        .filter(movie -> !favoriteMovies.contains(movie))
        .toList();

    if (!nonFavoriteMovies.isEmpty()) {
      Movie movieToAdd = nonFavoriteMovies.get(0);
      movieToAdd.addStarNumber();
      user.addMovie(movieToAdd);
      userRepository.save(user);
      return movieToAdd;
    }

    // if no movie is found, add a default movie
    Movie favoriteMovie = new Movie("Favorite Movie");

    if (user.getFavoriteMovies().contains(favoriteMovie)) {
      throw new ResourceNotFoundException("All existing movies already added to the favorite movie list.");
    }

    favoriteMovie.addStarNumber();
    movieRepository.save(favoriteMovie);
    user.addMovie(favoriteMovie);
    userRepository.save(user);
    return favoriteMovie;
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
