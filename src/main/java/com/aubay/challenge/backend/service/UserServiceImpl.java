package com.aubay.challenge.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
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

  private static final String ROLE_NOT_FOUND = "Role not found";
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

  public User create(User user) throws UserAlreadyExistsException, ResourceNotFoundException {

    if (userRepository.existsByUsername(user.getUsername())) {
      throw new UserAlreadyExistsException("User already exists for this username");
    }
    user.setPassword(bcryptEncoder.encode(user.getPassword()));
    Role role = extractRole("ROLE_USER");
    user.addRole(role);
    return userRepository.save(user);
  }

  public User addRole(Long id, RoleRequest newRole) throws ResourceNotFoundException {

    User user = extractUser(id);
    Role role = extractRole(newRole.name());
    user.addRole(role);
    return userRepository.save(user);
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

  public User removeMovie(Long id, String movieTitle) throws ResourceNotFoundException {

    User user = extractUser(id);
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

  private Role extractRole(String roleName) throws ResourceNotFoundException {

    Optional<Role> optionalRole = roleRepository.findByName(roleName);
    if (!optionalRole.isPresent()) {
      throw new ResourceNotFoundException(ROLE_NOT_FOUND);
    }
    return optionalRole.get();
  }
}
