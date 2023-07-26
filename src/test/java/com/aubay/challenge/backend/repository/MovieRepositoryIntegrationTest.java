package com.aubay.challenge.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import com.aubay.challenge.backend.entity.Movie;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MovieRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private MovieRepository MovieRepository;

  @Test
  public void whenFindByOriginalTitle_thenReturnsMovie() {
    // Given
    Movie titanic = new Movie("Titanic");
    entityManager.persistAndFlush(titanic);

    // When
    Movie found = MovieRepository.findByOriginalTitle(titanic.getOriginalTitle()).orElse(null);

    // Then
    assertThat(found.getOriginalTitle()).isEqualTo(titanic.getOriginalTitle());
  }

  @Test
  public void whenInvalidOriginalTitle_thenReturnsEmpty() {
    Optional<Movie> fromDb = MovieRepository.findByOriginalTitle("doesNotExist");
    assertTrue(fromDb.isEmpty());
  }

  @Test
  public void whenFindById_thenReturnsMovie() {
    // Given
    Movie movie = new Movie("Matrix");
    entityManager.persistAndFlush(movie);

    // When
    Movie fromDb = MovieRepository.findById(movie.getId()).orElse(null);

    // Then
    assertThat(fromDb.getOriginalTitle()).isEqualTo(movie.getOriginalTitle());
  }

  @Test
  public void whenInvalidId_thenReturnsNull() {
    Movie fromDb = MovieRepository.findById(-11l).orElse(null);
    assertThat(fromDb).isNull();
  }

  @Test
  public void givenSetOfMovies_whenFindAll_thenReturnsAllMovies() {
    // Given
    Movie matrix1 = new Movie("Matrix 1");
    Movie matrix2 = new Movie("Matrix 2");
    Movie matrix3 = new Movie("Matrix 3");

    entityManager.persist(matrix1);
    entityManager.persist(matrix2);
    entityManager.persist(matrix3);
    entityManager.flush();

    // When
    List<Movie> allMovies = MovieRepository.findAll();

    // Then
    assertThat(allMovies).hasSize(3).extracting(Movie::getOriginalTitle).containsOnly(matrix1.getOriginalTitle(),
        matrix2.getOriginalTitle(), matrix3.getOriginalTitle());
  }

  @Test
  public void givenSetOfMovies_whenFindTop10_thenReturnsCorrectMovies() {
    // Given
    List<Movie> movies =
        IntStream.rangeClosed(1, 12).mapToObj(i -> new Movie("Matrix " + i, i)).collect(Collectors.toList());
    movies.forEach(entityManager::persist);
    entityManager.flush();

    // When
    List<Movie> allMovies = MovieRepository.findTop10ByOrderByStarNumberDesc();

    // Then
    List<String> topMovies = IntStream.rangeClosed(3, 12).mapToObj(i -> "Matrix " + i).collect(Collectors.toList());
    assertThat(allMovies).hasSize(10).extracting(Movie::getOriginalTitle).containsAll(topMovies);
    assertThat(allMovies).extracting(Movie::getOriginalTitle).doesNotContain("Matrix 1");
  }
}
