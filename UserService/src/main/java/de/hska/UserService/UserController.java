package de.hska.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping
    public ResponseEntity<Long> addUser(@RequestBody User user) {
        UserRole role = this.roleRepository.findRoleByLevel(1);

        if (role == null) {
            role = this.roleRepository.save(new UserRole("user", 1));
        }

        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!checkUser(user)) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        user.setRole(role);
        userRepository.save(user);
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
