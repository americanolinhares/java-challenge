package com.aubay.challenge.backend.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class User {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty(message = "The username is required.")
  @Column(name = "username", nullable = false, unique = true, length = 45)
  private String username;

  @NotEmpty(message = "The password is required.")
  @Column(name = "password", nullable = false, length = 64)
  private String password;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "users_movies", joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "movie_id"))
  private Set<Movie> favoriteMovies = new HashSet<>();

  public User() {}

  public User(Long id, String username, String password, Set<Role> roles, Set<Movie> movies) {
    super();
    this.id = id;
    this.username = username;
    this.password = password;
    this.roles = roles;
    this.favoriteMovies = movies;
  }

  public User(String username, String password, Set<Role> roles) {
    super();
    this.username = username;
    this.password = password;
    this.roles = roles;
  }

  public void addRole(Role role) {
    this.roles.add(role);
  }

  public void addMovie(Movie movie) {
    this.favoriteMovies.add(movie);
  }

  public void removeMovie(Movie movie) {
    this.favoriteMovies.remove(movie);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<Movie> getFavoriteMovies() {
    return favoriteMovies;
  }

  public void setFavoriteMovies(Set<Movie> favoriteMovies) {
    this.favoriteMovies = favoriteMovies;
  }
}
