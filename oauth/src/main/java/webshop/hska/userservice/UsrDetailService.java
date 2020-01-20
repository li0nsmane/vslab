package webshop.hska.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import webshop.hska.oauth.clients.UserClient;
import webshop.hska.oauth.model.User;
import webshop.hska.oauth.model.UsrDetails;

@Component
public class UsrDetailService implements UserDetailsService {

	@Autowired
	private UserClient userClient;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userClient.getUserByUsername(username);
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
