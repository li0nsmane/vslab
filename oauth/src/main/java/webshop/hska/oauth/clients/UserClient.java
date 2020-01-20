package webshop.hska.oauth.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import webshop.hska.oauth.model.User;

@Component
public class UserClient {

	@Autowired
	private RestTemplate restTemplate;

	public User getUserByUsername(String username) {
		return restTemplate.getForObject("http://user-service/users/" + username, User.class);
	}
}
