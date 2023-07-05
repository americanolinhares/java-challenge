package com.aubay.challenge.backend.service;

import java.util.List;

import com.aubay.challenge.backend.entity.User;

public interface UserService {

	public List<User> listAll();

	public User save(User user);

	public void registerDefaultUser(User user);

}