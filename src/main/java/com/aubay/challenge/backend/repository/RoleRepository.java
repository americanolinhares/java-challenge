package com.aubay.challenge.backend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.aubay.challenge.backend.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	@Query("SELECT r FROM Role r WHERE r.name = :name")
	Role findByName(@Param("name") String name);
}