package com.aubay.challenge.backend.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.aubay.challenge.backend.entity.Role;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.entity.requests.RoleRequest;
import com.aubay.challenge.backend.exception.UserAlreadyExistsException;
import com.aubay.challenge.backend.repository.RoleRepository;
import com.aubay.challenge.backend.repository.UserRepository;

class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testListAll() {
    // Prepare test data
    List<User> userList = new ArrayList<>();
    User user1 = new User();
    User user2 = new User();
    userList.add(user1);
    userList.add(user2);

    // Mock the repository behavior
    when(userRepository.findAll()).thenReturn(userList);

    // Call the service method
    List<User> result = userService.listAll();

    // Verify the result
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(userList, result);
  }

  @Test
  void testEdit() {
    // Prepare test data
    Long userId = 1L;
    RoleRequest newRole = new RoleRequest("ROLE_ADMIN");

    User user = new User();
    Role role = new Role(1L, "ROLE_ADMIN");
    user.addRole(role);

    // Mock the repository behavior
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(roleRepository.findByName(newRole.name())).thenReturn(Optional.of(role));
    when(userRepository.save(user)).thenReturn(user);

    // Call the service method
    // User result = userService.edit(userId, newRole);

    // Verify the result
    // Assertions.assertEquals(user, result);
    // Assertions.assertTrue(user.getRoles().contains(role));
  }

  @Test
  void testRegisterDefaultUser() throws UserAlreadyExistsException {
    // Prepare test data
    User user = new User();
    user.setPassword("password");

    Role role = new Role(1L, "ROLE_USER");

    // Mock the repository behavior
    when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
    when(userRepository.save(user)).thenReturn(user);

    // Call the service method
    User result = userService.create(user);

    // Verify the result
    Assertions.assertEquals(user, result);
    Assertions.assertTrue(user.getRoles().contains(role));
    verify(passwordEncoder).encode("password");
  }
}
