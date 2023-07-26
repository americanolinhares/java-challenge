package com.aubay.challenge.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aubay.challenge.backend.entity.JwtResponse;
import com.aubay.challenge.backend.entity.requests.UserRequest;
import com.aubay.challenge.backend.security.JwtUtils;
import com.aubay.challenge.backend.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "authentication", description = "Generate the authentication token")
@RestController
@RequestMapping("/login")
public class AuthenticationController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping
  @Operation(summary = "Generate the auth token of an existing user", tags = {"authentication"})
  public ResponseEntity<JwtResponse> login(@RequestBody @Valid UserRequest request) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    String jwt = jwtUtils.generateJwtToken(authentication);

    Object obj = authentication.getPrincipal();
    UserDetails userDetails = (UserDetailsImpl) obj;

    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).toList();

    return ResponseEntity
        .ok(new JwtResponse(jwt, ((UserDetailsImpl) userDetails).getId(), userDetails.getUsername(), roles));
  }
}
