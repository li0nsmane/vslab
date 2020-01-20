package hska.webshop.userservice.repositories;

import org.springframework.data.repository.CrudRepository;

import hska.webshop.userservice.model.User;

public interface UserRepo extends CrudRepository<User, Long> {
	
	public User findUserByUsername(String username);
}
