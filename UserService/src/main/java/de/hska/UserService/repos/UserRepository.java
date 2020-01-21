package de.hska.UserService.repos;

import org.springframework.data.repository.CrudRepository;

import de.hska.UserService.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    public User findUserByUsername(String username);

}
