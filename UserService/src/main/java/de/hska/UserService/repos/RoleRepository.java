package de.hska.UserService.repos;

import org.springframework.data.repository.CrudRepository;

import de.hska.UserService.model.UserRole;

public interface RoleRepository extends CrudRepository<UserRole, Long> {

    UserRole findRoleByLevel(int level);
}
