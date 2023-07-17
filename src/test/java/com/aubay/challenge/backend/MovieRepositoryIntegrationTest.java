package com.aubay.challenge.backend;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import com.aubay.challenge.backend.entity.Movie;
import com.aubay.challenge.backend.repository.MovieRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MovieRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private MovieRepository MovieRepository;

  @Test
  public void whenFindByOriginalTitle_thenReturnMovie() {
    Movie titanic = new Movie("Titanic");
    entityManager.persistAndFlush(titanic);

    Movie found = MovieRepository.findByOriginalTitle(titanic.getOriginalTitle()).orElse(null);
    assertThat(found.getOriginalTitle()).isEqualTo(titanic.getOriginalTitle());
  }

  @Test
  public void whenInvalidOriginalTitle_thenReturnEmpty() {
    Optional<Movie> fromDb = MovieRepository.findByOriginalTitle("doesNotExist");
    assertTrue(fromDb.isEmpty());
  }

  @Test
  public void whenFindById_thenReturnMovie() {
    Movie movie = new Movie("Matrix");
    entityManager.persistAndFlush(movie);

    Movie fromDb = MovieRepository.findById(movie.getId()).orElse(null);
    assertThat(fromDb.getOriginalTitle()).isEqualTo(movie.getOriginalTitle());
  }

  @Test
  public void whenInvalidId_thenReturnNull() {
    Movie fromDb = MovieRepository.findById(-11l).orElse(null);
    assertThat(fromDb).isNull();
  }

  @Test
  public void givenSetOfMovies_whenFindAll_thenReturnAllMovies() {
    Movie matrix1 = new Movie("Matrix 1");
    Movie matrix2 = new Movie("Matrix 2");
    Movie matrix3 = new Movie("Matrix 3");

    entityManager.persist(matrix1);
    entityManager.persist(matrix2);
    entityManager.persist(matrix3);
    entityManager.flush();

    List<Movie> allMovies = MovieRepository.findAll();

    assertThat(allMovies).hasSize(3).extracting(Movie::getOriginalTitle).containsOnly(matrix1.getOriginalTitle(),
        matrix2.getOriginalTitle(), matrix3.getOriginalTitle());
  }

  @Test
  public void givenSetOfMovies_whenFindfindTop10_thenReturnCorrectMovies() {
    Movie matrix1 = new Movie("Matrix 1", 3);
    Movie matrix2 = new Movie("Matrix 2", 4);
    Movie matrix3 = new Movie("Matrix 3", 5);
    Movie matrix4 = new Movie("Matrix 4", 6);
    Movie matrix5 = new Movie("Matrix 5", 7);
    Movie matrix6 = new Movie("Matrix 6", 8);
    Movie matrix7 = new Movie("Matrix 7", 9);
    Movie matrix8 = new Movie("Matrix 8", 10);
    Movie matrix9 = new Movie("Matrix 9", 11);
    Movie matrix10 = new Movie("Matrix 10", 12);
    Movie matrix11 = new Movie("Matrix 11", 13);
    Movie matrix12 = new Movie("Matrix 12", 14);

    entityManager.persist(matrix1);
    entityManager.persist(matrix2);
    entityManager.persist(matrix3);
    entityManager.persist(matrix4);
    entityManager.persist(matrix5);
    entityManager.persist(matrix6);
    entityManager.persist(matrix7);
    entityManager.persist(matrix8);
    entityManager.persist(matrix9);
    entityManager.persist(matrix10);
    entityManager.persist(matrix11);
    entityManager.persist(matrix12);
    entityManager.flush();

    List<Movie> allMovies = MovieRepository.findTop10ByOrderByStarNumberDesc();

    assertThat(allMovies).hasSize(10).extracting(Movie::getOriginalTitle).containsOnly(matrix3.getOriginalTitle(),
        matrix4.getOriginalTitle(), matrix5.getOriginalTitle(), matrix6.getOriginalTitle(), matrix7.getOriginalTitle(),
        matrix6.getOriginalTitle(), matrix7.getOriginalTitle(), matrix8.getOriginalTitle(), matrix9.getOriginalTitle(),
        matrix10.getOriginalTitle(), matrix11.getOriginalTitle(), matrix12.getOriginalTitle());
  }
}
