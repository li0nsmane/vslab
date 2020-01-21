package hska.iwi.eShopMaster.controller.oauth;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.web.client.RestTemplate;


public class Oauth {
	
		private static final String TOKEN_URI = "http://zuul:8020/auth/oauth/token";
		private static final String CLIENT_ID = "messaging-client";
		private static final String CLIENT_SECRET = "secret";
		private static final String SCOPE = "openid";
		private static final String GRANT_TYPE = "password";
		private static OAuth2RestTemplate OAUTH2_REST_TEMPLATE;
		
		public static RestTemplate getDefaultRestTemplate() {
			return new RestTemplate();
		}

	    public static OAuth2RestTemplate getOAuth2RestTemplate() {
	        return OAUTH2_REST_TEMPLATE;
	    }

	    public static OAuth2RestTemplate createOAuth2RestTemplate(String username, String password) {
	        AccessTokenRequest atr = new DefaultAccessTokenRequest();
	        OAUTH2_REST_TEMPLATE = new OAuth2RestTemplate(createProtectedResourceDetails(username, password),
	                new DefaultOAuth2ClientContext(atr));
	        System.out.println(username + password);
	        return OAUTH2_REST_TEMPLATE;
	    }

	    private static OAuth2ProtectedResourceDetails createProtectedResourceDetails(String username, String password) {
	        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
	        resource.setClientId(CLIENT_ID);
	        resource.setClientSecret(CLIENT_SECRET);
	        resource.setGrantType(GRANT_TYPE);
	        ArrayList<String> scopes = new ArrayList<String>();
	        scopes.add(SCOPE);
	        resource.setScope(scopes);
	        resource.setAccessTokenUri(TOKEN_URI);
	        
	        resource.setUsername(username);
	        resource.setPassword(password);

	        return resource;
	    }

		public static void deleteOAuth2RestTemplate() {
			OAUTH2_REST_TEMPLATE = null;
			
		}

}
