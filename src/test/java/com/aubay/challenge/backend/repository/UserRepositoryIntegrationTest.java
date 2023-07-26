package com.aubay.challenge.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.aubay.challenge.backend.entity.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private UserRepository UserRepository;

  @Test
  public void whenSaveUser_thenReturnUser() {
    // Given
    User pedro = new User("pedro", "123456", null);

    // When
    User found = UserRepository.save(pedro);

    // Then
    assertNotNull(found.getId());
    assertThat(pedro.getUsername()).isEqualTo(found.getUsername());
    assertThat(pedro.getPassword()).isEqualTo(found.getPassword());
  }

  @Test
  public void whenInvalidOriginalTitle_thenReturnEmpty() {
    Optional<User> fromDb = UserRepository.findByUsername("doesNotExist");
    assertTrue(fromDb.isEmpty());
  }

  @Test
  public void whenInvalidId_thenReturnNull() {
    User fromDb = UserRepository.findById(-11l).orElse(null);
    assertThat(fromDb).isNull();
  }

}
