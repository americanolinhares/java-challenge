package com.aubay.challenge.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aubay.challenge.backend.entity.NewRole;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.service.UserServiceImpl;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

	@Autowired
	UserServiceImpl service;

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<List<User>> getAllTodos() {
		List<User> users = service.listAll();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/userboard")
	@PreAuthorize("hasRole('USER')")
	public String userAccess() {
		return "User Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@PostMapping("/registration")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> createUser(@RequestBody User user) {

		User usr = service.registerDefaultUser(user);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("user", "/user/" + user.getId().toString());
		return new ResponseEntity<>(usr, httpHeaders, HttpStatus.CREATED);
	}

	@PutMapping("/{id}/roles")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> editUser(@PathVariable Long id, @RequestBody NewRole role) {

		User usr = service.edit(id, role);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("user", "/user/" + id.toString());
		return new ResponseEntity<>(usr, httpHeaders, HttpStatus.ACCEPTED);
	}

}
