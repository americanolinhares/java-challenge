package com.aubay.challenge.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.entity.Role;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.entity.requests.MovieRequest;
import com.aubay.challenge.backend.entity.requests.RoleRequest;
import com.aubay.challenge.backend.exception.ResourceNotFoundException;
import com.aubay.challenge.backend.exception.UserAlreadyExistsException;
import com.aubay.challenge.backend.repository.MovieRepository;
import com.aubay.challenge.backend.repository.RoleRepository;
import com.aubay.challenge.backend.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl {

  private static final String MOVIE_NOT_FOUND = "Movie not found";
  private static final String USER_NOT_FOUND = "User not found";

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  MovieRepository movieRepository;

  @Autowired
  private BCryptPasswordEncoder bcryptEncoder;

  // TODO REMOVE
  public List<User> listAll() {
    return userRepository.findAll();
  }

  public User create(User user) throws UserAlreadyExistsException {

    if (userRepository.existsByUsername(user.getUsername())) {
      throw new UserAlreadyExistsException("User already exists for this username");
    }

    user.setPassword(bcryptEncoder.encode(user.getPassword()));
    Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"));
    user.addRole(role);

    return userRepository.save(user);
  }

  public User addRole(Long id, RoleRequest newRole) throws ResourceNotFoundException {
    User user;
    try {
      user = userRepository.findById(id).get();
    } catch (Exception e) {
      throw new ResourceNotFoundException(USER_NOT_FOUND);
    }

    Role role;
    try {
      role = roleRepository.findByName(newRole.name()).get();
    } catch (Exception e) {
      throw new ResourceNotFoundException("Role not found");
    }

    user.addRole(role);
    return userRepository.save(user);
  }

  public Movie addMovie(Long id, MovieRequest newMovie) throws ResourceNotFoundException {

    User user;
    try {
      user = userRepository.findById(id).get();
    } catch (Exception e) {
      throw new ResourceNotFoundException(USER_NOT_FOUND);
    }

    Movie movie;
    try {
      movie = movieRepository.findByOriginalTitle(newMovie.title()).get();
    } catch (Exception e) {
      throw new ResourceNotFoundException(MOVIE_NOT_FOUND);
    }
    movie.addStarNumber();
    user.addMovie(movie);
    userRepository.save(user);

    return movie;
  }

  public User removeMovie(Long id, MovieRequest newMovie) throws ResourceNotFoundException {

    User user;
    try {
      user = userRepository.findById(id).get();
    } catch (Exception e) {
      throw new ResourceNotFoundException(USER_NOT_FOUND);
    }
    Movie movie;
    try {
      movie = movieRepository.findByOriginalTitle(newMovie.title()).get();
    } catch (Exception e) {
      throw new ResourceNotFoundException(MOVIE_NOT_FOUND);
    }

    if (!user.getFavoriteMovies().contains(movie)) {
      throw new ResourceNotFoundException("That movie does not belong to the user's favorite list.");
    }

    movie.subtractStarNumber();
    user.removeMovie(movie);

    return userRepository.save(user);
  }

  public Set<Movie> listFavoriteMovies() throws ResourceNotFoundException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = "";
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      username = authentication.getName();
    } else {
      throw new ResourceNotFoundException(USER_NOT_FOUND);
    }

    Optional<User> persistedUser = userRepository.findByUsername(username);
    if (persistedUser.isPresent()) {
      return persistedUser.get().getFavoriteMovies();
    } else {
      throw new ResourceNotFoundException(USER_NOT_FOUND);
    }
  }

}
