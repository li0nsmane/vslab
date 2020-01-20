package hska.webshop.userservice.repositories;

import org.springframework.data.repository.CrudRepository;

import hska.webshop.userservice.model.Role;

public interface RoleRepo extends CrudRepository<Role, Long> {
	Role findRoleByLevel(int level);
}
