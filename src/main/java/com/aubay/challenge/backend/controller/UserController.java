package com.aubay.challenge.backend.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	UserService service;

	public UserController(UserService userService) {
		this.service = userService;
	}

	@GetMapping
	public ResponseEntity<List<User>> getAllTodos() {
		List<User> users = service.listAll();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<User> saveUser(@RequestBody User user) {

		User usr = service.save(user);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("user", "/user/" + user.getId().toString());
		return new ResponseEntity<>(usr, httpHeaders, HttpStatus.CREATED);
	}

}
