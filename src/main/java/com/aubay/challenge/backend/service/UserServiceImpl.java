package com.aubay.challenge.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aubay.challenge.backend.entity.NewRole;
import com.aubay.challenge.backend.entity.Role;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.repository.RoleRepository;
import com.aubay.challenge.backend.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl {

	@Autowired
	UserRepository userRepo;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	public List<User> listAll() {

		List<User> users = new ArrayList<>();
		userRepo.findAll().forEach(users::add);
		return users;
	}

	public User edit(Long id, NewRole newRole) {

		User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		Role role = roleRepo.findByName(newRole.name()).orElseThrow(() -> new RuntimeException("Role not found"));

		user.addRole(role);

		return userRepo.save(user);
	}

	public User registerDefaultUser(User user) {

		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		Role role = roleRepo.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"));

		user.addRole(role);

		return userRepo.save(user);
	}

}