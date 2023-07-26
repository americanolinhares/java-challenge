package com.aubay.challenge.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aubay.challenge.backend.entity.Role;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.entity.requests.RoleRequest;
import com.aubay.challenge.backend.exception.ResourceNotFoundException;
import com.aubay.challenge.backend.exception.UserAlreadyExistsException;
import com.aubay.challenge.backend.repository.RoleRepository;
import com.aubay.challenge.backend.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl {

  private static final String ROLE_NOT_FOUND = "Role not found";
  private static final String USER_NOT_FOUND = "User not found";

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

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
    Role role = findRole("ROLE_USER");
    user.addRole(role);
    return userRepository.save(user);
  }

  public User addRole(Long id, RoleRequest newRole) throws ResourceNotFoundException {

    User user = findUser(id);
    Role role = findRole(newRole.name());
    user.addRole(role);
    return userRepository.save(user);
  }

  private User findUser(Long id) throws ResourceNotFoundException {

    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isEmpty()) {
      throw new ResourceNotFoundException(USER_NOT_FOUND);
    }
    return optionalUser.get();
  }

  private Role findRole(String roleName) throws ResourceNotFoundException {

    Optional<Role> optionalRole = roleRepository.findByName(roleName);
    if (!optionalRole.isPresent()) {
      throw new ResourceNotFoundException(ROLE_NOT_FOUND);
    }
    return optionalRole.get();
  }
}
