package com.aubay.challenge.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import com.aubay.challenge.backend.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

  Optional<Movie> findByOriginalTitle(@Param("title") String title);

  List<Movie> findTop10ByOrderByStarNumberDesc();

}
