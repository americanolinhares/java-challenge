package com.aubay.challenge.backend.service;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.entity.NewRole;
import com.aubay.challenge.backend.entity.RequestMovie;
import com.aubay.challenge.backend.entity.Role;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.repository.MovieRepository;
import com.aubay.challenge.backend.repository.RoleRepository;
import com.aubay.challenge.backend.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl {

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  MovieRepository movieRepository;

  @Autowired
  private BCryptPasswordEncoder bcryptEncoder;

  public List<User> listAll() {
    return userRepository.findAll();
  }

  public Set<Movie> listFavoriteMovies(String username) {

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return user.getFavoriteMovies();
  }

  public User edit(Long id, NewRole newRole) {

    User user =
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    Role role = roleRepository.findByName(newRole.name())
        .orElseThrow(() -> new RuntimeException("Role not found"));
    user.addRole(role);

    return userRepository.save(user);
  }

  public User addMovie(Long id, RequestMovie newMovie) {

    User user =
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    Movie movie = movieRepository.findByOriginalTitle(newMovie.title())
        .orElseThrow(() -> new RuntimeException("Movie not found"));
    movie.addStarNumber();
    user.addMovie(movie);

    return userRepository.save(user);
  }

  public User removeMovie(Long id, RequestMovie newMovie) {

    User user =
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    Movie movie = movieRepository.findByOriginalTitle(newMovie.title())
        .orElseThrow(() -> new RuntimeException("Movie not found"));
    movie.subtractStarNumber();
    user.removeMovie(movie);

    return userRepository.save(user);
  }

  public User registerDefaultUser(User user) {

    user.setPassword(bcryptEncoder.encode(user.getPassword()));
    Role role = roleRepository.findByName("ROLE_USER")
        .orElseThrow(() -> new RuntimeException("Role not found"));
    user.addRole(role);

    return userRepository.save(user);
  }

}
