package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.model.database.dataobjects.UserLogin;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 
 * @author knad0001
 */

public class UserManagerImpl implements UserManager {

	
	public UserManagerImpl() {

	}

	private static final String USER_URL = "http://zuul:8020/users";




	public User login(String username, String password) {
		UserLogin u = new UserLogin(username, password);
		if (username == null || username.equals("")) {
			return null;
		}
		try {
			OAuth2RestTemplate oAuth2RestTemplate = Oauth.createOAuth2RestTemplate(username, password);
			User user = oAuth2RestTemplate.getForEntity(USER_URL + "/" + u.getUsername(), User.class).getBody();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void registerUser(String username, String name, String lastname, String password, Role role) {


		User user = new User(username, name, lastname, password, new Role("", 1));
		System.out.println("registerUser in eshop userm");
		RestTemplate oAuth2RestTemplate = Oauth.getDefaultRestTemplate();
		ResponseEntity<Long> id = oAuth2RestTemplate.postForEntity(USER_URL, user, Long.class);
	}

	
	public User getUserByUsername(String username) {
		if (username == null || username.equals("")) {
			return null;
		}
		String encodedUserName = "";
		try {
			encodedUserName= URLDecoder.decode(username, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			return Oauth.getDefaultRestTemplate().getForObject(USER_URL + "/" + encodedUserName, User.class);

		}catch (HttpClientErrorException ex){
			return null;
		}
	}

	public boolean deleteUserById(int id) {
		OAuth2RestTemplate oAuth2RestTemplate = Oauth.getOAuth2RestTemplate();
		oAuth2RestTemplate.delete(USER_URL + "/" + id);
		return true;

	}

	public Role getRoleByLevel(int level) {
		return level == 0 ? new Role("admin", 0) : new Role("user", 1);
	}

	public boolean doesUserAlreadyExist(String username) {
		
    	User dbUser = this.getUserByUsername(username);
    	
    	if (dbUser != null){
    		return true;
    	}
    	else {
    		return false;
    	}
	}
	

	public boolean validate(User user) {
		if (user.getFirstname().isEmpty() || user.getPassword().isEmpty() || user.getRole() == null || user.getLastname() == null || user.getUsername() == null) {
			return false;
		}
		
		return true;
	}

}
