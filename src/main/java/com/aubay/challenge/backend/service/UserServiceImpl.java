package com.aubay.challenge.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aubay.challenge.backend.entity.Role;
import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.repository.RoleRepository;
import com.aubay.challenge.backend.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	RoleRepository roleRepo;

	@Override
	public List<User> listAll() {

		List<User> users = new ArrayList<>();
		userRepo.findAll().forEach(users::add);
		return users;
	}

	/*
	 * @Override public User save(User user) { return userRepo.save(user); }
	 */

	@Override
	public User save(User user) {
		Role roleUser = roleRepo.findByName("USER");
		user.addRole(roleUser);

		return userRepo.save(user);
	}

	@Override
	public void registerDefaultUser(User user) {
		Role roleUser = roleRepo.findByName("USER");
		user.addRole(roleUser);

		userRepo.save(user);
	}

}