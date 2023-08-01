package com.aubay.challenge.backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.exception.ResourceNotFoundException;
import com.aubay.challenge.backend.repository.MovieRepository;
import com.aubay.challenge.backend.repository.UserRepository;

@Service
public class RandomMovies {

  @Autowired
  UserRepository userRepository;

  @Autowired
  MovieRepository movieRepository;

  private static final String USER_NOT_FOUND = "User not found";

  public String getNewSuggestedMovie()
      throws ResourceNotFoundException {

    UserDetailsImpl userDetails = findUserPrincipal();
    User currentUser = findUser(userDetails.getId());

    Set<String> currentUserFavoriteMovies = currentUser.getFavoriteMovies().stream()
        .map(Movie::getOriginalTitle)
        .collect(Collectors.toSet());

    List<Set<String>> otherUsersFavoriteMovies = userRepository.findAll().stream()
        .filter(user -> !user.equals(currentUser))
        .map(User::getFavoriteMovies)
        .map(movies -> movies.stream().map(Movie::getOriginalTitle).collect(Collectors.toSet()))
        .toList();

    int maxMatch = 0;
    Set<String> setWithGreatestMatch = new HashSet<>();

    for (Set<String> favoriteMovies : otherUsersFavoriteMovies) {
      int matchCount = 0;
      for (String movie : favoriteMovies) {
        if (currentUserFavoriteMovies.contains(movie)) {
          matchCount++;
        }
      }

      if (matchCount > maxMatch) {
        maxMatch = matchCount;
        setWithGreatestMatch = favoriteMovies;
      }
    }

    setWithGreatestMatch.removeAll(currentUserFavoriteMovies);

    return setWithGreatestMatch.stream().findFirst().orElse(null);

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
}
