package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.oauth.OAuthConfig;

public class UserManagerImpl implements UserManager {
	private static final String USER_URI = "http://zuul:8770/users";

	public UserManagerImpl() {
	}

	public void registerUser(String username, String name, String lastname, String password) {
		User user = new User(username, name, lastname, password);
		OAuthConfig.getDefaultRestTemplate().postForObject(USER_URI, user, Long.class);
	}

	public User getUser(String username, String password) {
		if (username == null || username.equals("")) {
			return null;
		}
		try {
			OAuth2RestTemplate oAuth2RestTemplate = OAuthConfig.createOAuth2RestTemplate(username, password);
			User user = oAuth2RestTemplate.getForObject(USER_URI + "/{username}", User.class, username);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean deleteUserById(int id) {
		OAuthConfig.getOAuth2RestTemplate().delete(USER_URI + "/{id}", id);
		return true;
	}

//	public Role getRoleByLevel(int levelId) {
//		Role role = OAuthConfig.getOAuth2RestTemplate().getForObject(USER_URI + "/roles/{levelId}", Role.class, levelId);
//		return role;
//	}

	public boolean doesUserAlreadyExist(String username) {
		try {
			User user = OAuthConfig.getDefaultRestTemplate().getForObject(USER_URI + "/{username}", User.class, username);
			return user != null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean validate(User user) {
		if (user.getFirstname().isEmpty() || user.getPassword().isEmpty() || user.getRole() == null
				|| user.getLastname() == null || user.getUsername() == null) {
			return false;
		}

		return true;
	}

}