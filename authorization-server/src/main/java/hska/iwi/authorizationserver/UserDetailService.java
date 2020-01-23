package hska.iwi.authorizationserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailService implements UserDetailsService {
    @Autowired
    UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ShopUser user = null;
        if(username.equals("enduser")) {

            Role role = new Role("user", 1);
            user = new ShopUser("enduser", "Leonie", "H", "password", role);
        } else if(username.equals("admin")) {

            Role role = new Role("admin", 0);
            user = new ShopUser("admin", "LeonieA", "H", "password", role);
        }
        else {
            user = userClient.getUserByUsername(username);

        }
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        String role;
        if (user.getRole().getLevel() == 0) {
            role = "ROLE_ADMIN";
        } else {
            role = "ROLE_USER";
        }
        UserDetails userDetails = new UsrDetails(user.getId(), user.getUsername(), user.getPassword(), role);
        return userDetails;
    }
}
