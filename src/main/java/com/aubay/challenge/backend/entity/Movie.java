package com.aubay.challenge.backend.entity;

import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movies")
public class Movie implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("id")
  private Long id;

  @Column(name = "original_title", nullable = false, unique = true, length = 45)
  @JsonProperty("original_title")
  private String originalTitle;

  @Column(name = "star_number", nullable = false)
  @JsonProperty("star_number")
  private int starNumber;

  public Movie() {}

  public Movie(String originalTitle) {
    super();
    this.originalTitle = originalTitle;
    this.starNumber = 0;
  }

  public Movie(Long id, String originalTitle) {
    super();
    this.id = id;
    this.originalTitle = originalTitle;
    this.starNumber = 0;
  }

  public Movie(String originalTitle, int starNumber) {
    super();
    this.originalTitle = originalTitle;
    this.starNumber = starNumber;
  }

  public Movie(Long id, String originalTitle, int starNumber) {
    super();
    this.id = id;
    this.originalTitle = originalTitle;
    this.starNumber = starNumber;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }

  public int getStarNumber() {
    return starNumber;
  }

  public void setStarNumber(int starNumber) {
    this.starNumber = starNumber;
  }

  public void addStarNumber() {
    this.starNumber += 1;
  }

  public void subtractStarNumber() {
    this.starNumber -= 1;
  }

  @Override
  public String toString() {
    return "Movie [id=" + id + ", originalTitle=" + originalTitle + ", starNumber=" + starNumber + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(originalTitle);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Movie other = (Movie) obj;
    return Objects.equals(originalTitle, other.getOriginalTitle());
  }

}
