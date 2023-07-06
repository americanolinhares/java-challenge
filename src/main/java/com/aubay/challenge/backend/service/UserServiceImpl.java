package com.aubay.challenge.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public List<User> listAll() {

		List<User> users = new ArrayList<>();
		userRepo.findAll().forEach(users::add);
		return users;
	}

	/*
	 * @Override public User save(User user) { return userRepo.save(user); }
	 */

	public User save(User user) {
		Optional<Role> roleUser = roleRepo.findByName("ROLE_USER");
		user.addRole(roleUser.get());

		return userRepo.save(user);
	}

	public void registerDefaultUser(User user) {
		Optional<Role> roleUser = roleRepo.findByName("ROLE_USER");
		user.addRole(roleUser.get());

		userRepo.save(user);
	}

}