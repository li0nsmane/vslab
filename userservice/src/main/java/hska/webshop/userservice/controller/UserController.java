package hska.webshop.userservice.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hska.webshop.userservice.model.Role;
import hska.webshop.userservice.model.User;
import hska.webshop.userservice.model.UserLevel;
import hska.webshop.userservice.repositories.RoleRepo;
import hska.webshop.userservice.repositories.UserRepo;


@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;
	
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
		try {
			username = URLDecoder.decode(username, "UTF-8");
			User user = userRepo.findUserByUsername(username);
			if (user != null) {
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Long> registerUser(@RequestBody User user) {
		Role role = this.roleRepo.findRoleByLevel(UserLevel.CUSTOMER.getLevelId());
		if (userRepo.findUserByUsername(user.getUsername()) == null) {
			if (user.getFirstname() == null || user.getFirstname().isEmpty() || user.getLastname() == null
					|| user.getLastname().isEmpty() || user.getUsername() == null || user.getUsername().isEmpty()
					|| user.getPassword() == null || user.getPassword().isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				user.setRole(role);
				userRepo.save(user);
				long userId = this.userRepo.findUserByUsername(user.getUsername()).getId();
				return new ResponseEntity<Long>(userId, HttpStatus.CREATED);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUser(@PathVariable long id) {
		if (userRepo.findById(id).isPresent()) {
			userRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

//	@RequestMapping(value = "/roles/{levelId}", method = RequestMethod.GET)
//	public ResponseEntity<Role> getRoleByLevel(@PathVariable int levelId) {
//		Role role = this.roleRepo.findRoleByLevel(levelId);
//		if (role != null) {
//			return new ResponseEntity<Role>(role, HttpStatus.OK);
//		}
//		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	}
}
