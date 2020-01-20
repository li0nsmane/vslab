package hska.iwi.authorizationserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailService implements UserDetailsService {
    @Autowired
    UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ShopUser user = userClient.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        String role;
        if (user.getRole().getLevel() == 0) {
            role = "ADMIN";
        } else {
            role = "USER";
        }
        UserDetails userDetails = new UsrDetails(user.getId(), user.getUsername(), user.getPassword(), role);
        return userDetails;
    }
}
