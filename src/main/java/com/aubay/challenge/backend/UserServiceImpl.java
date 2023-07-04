package com.aubay.challenge.backend;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aubay.challenge.backend.entity.User;
import com.aubay.challenge.backend.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	UserRepository repo;

	public UserServiceImpl(UserRepository userRepository) {
		this.repo = userRepository;
	}

	@Override
	public List<User> listAll() {

		List<User> users = new ArrayList<>();
		repo.findAll().forEach(users::add);
		return users;
	}

	@Override
	public User save(User user) {
		return repo.save(user);
	}

}