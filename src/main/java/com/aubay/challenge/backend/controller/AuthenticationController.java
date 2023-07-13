package com.aubay.challenge.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aubay.challenge.backend.entity.AuthRequest;
import com.aubay.challenge.backend.entity.JwtResponse;
import com.aubay.challenge.backend.repository.RoleRepository;
import com.aubay.challenge.backend.repository.UserRepository;
import com.aubay.challenge.backend.security.JwtUtils;
import com.aubay.challenge.backend.service.UserDetailsImpl;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping
  public ResponseEntity<JwtResponse> login(@RequestBody @Valid AuthRequest request) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    List<String> roles =
        userDetails.getAuthorities().stream().map(item -> item.getAuthority()).toList();

    return ResponseEntity
        .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
  }
}


