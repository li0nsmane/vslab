package hska.iwi.eShopMaster.controller.manager;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import hska.iwi.eShopMaster.controller.oauth.Oauth;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import hska.iwi.eShopMaster.model.database.dataobjects.UserLogin;
import hska.iwi.eShopMaster.model.database.dataobjects.UserRegistration;


public class UserManagerImpl {

	private static final String USER_URL = "http://zuul:8020/users";

	public void registerUser(String username, String name, String lastname, String password, String password2) {
		UserRegistration user = new UserRegistration(username, name, lastname, password, password2);

		OAuth2RestTemplate oAuth2RestTemplate = Oauth.getOAuth2RestTemplate();
		oAuth2RestTemplate.postForEntity(USER_URL, user, User.class);
	}

	public User login(String username, String password) {
		UserLogin u = new UserLogin(username, password);
		if (username == null || username.equals("")) {
			return null;
		}
		try {
			OAuth2RestTemplate oAuth2RestTemplate = Oauth.createOAuth2RestTemplate(username, password);
			System.out.println("managerImpl" + oAuth2RestTemplate.getAccessToken().getTokenType());
			User user = oAuth2RestTemplate.getForEntity(USER_URL + "/" + u.getUsername(), User.class).getBody();
			System.out.println("managerImpl get call" + oAuth2RestTemplate.toString());
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public User getUserByUsername(String username) {
		if (username == null || username.equals("")) {
			return null;
		}

		return Oauth.getDefaultRestTemplate().getForEntity(USER_URL + "/" + username, User.class).getBody();
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

		if (dbUser != null) {
			return true;
		} else {
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
