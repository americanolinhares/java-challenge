package com.aubay.challenge.backend;

import java.util.List;

import com.aubay.challenge.backend.entity.User;

public interface UserService {

	public List<User> listAll();

	public User save(User user);

}