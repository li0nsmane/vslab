package de.hska.UserService;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<UserRole, Long> {

    UserRole findRoleByLevel(int level);
}
