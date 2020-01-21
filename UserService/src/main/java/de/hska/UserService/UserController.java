package de.hska.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import de.hska.UserService.model.User;
import de.hska.UserService.model.UserRole;
import de.hska.UserService.repos.RoleRepository;
import de.hska.UserService.repos.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @GetMapping(value = "/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {

        if(username.equals("enduser")) {
            UserRole role = new UserRole("user", 1);
            User user = new User("enduser", "Leonie", "H", "password", role);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else if(username.equals("admin")) {
            UserRole role = new UserRole("admin", 0);
            User user = new User("admin", "LeonieA", "H", "password", role);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            User user = userRepository.findUserByUsername(username);
            if (user != null) {
                return new ResponseEntity<User>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    @PostMapping
    public ResponseEntity<Long> addUser(@RequestBody User user) {
        System.out.println("addUser: " + user.getPassword());
        UserRole role = this.roleRepository.findRoleByLevel(1);
        if (role == null) {
            role = this.roleRepository.save(new UserRole("user", 1));
        }
        System.out.println("role: " + role.getTyp());
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!checkUser(user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setRole(role);
        userRepository.save(user);
        System.out.println("check if user was created: "+ this.userRepository.findUserByUsername(user.getUsername()).getId());
        long userId = this.userRepository.findUserByUsername(user.getUsername()).getId();
        return new ResponseEntity<Long>(userId, HttpStatus.CREATED);

    }

    private boolean checkUser(User u) {
        return checkNotNullNotEmpty(u.getFirstname()) && checkNotNullNotEmpty(u.getLastname()) && checkNotNullNotEmpty(u.getPassword());


    }

    private boolean checkNotNullNotEmpty(String s) {
        if (s != null && !s.isEmpty()) {
            return true;
        }
        return false;
    }

    // @PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Optional<User> user = userRepository.findById(id);
        if (user != null && !user.isEmpty()) {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
