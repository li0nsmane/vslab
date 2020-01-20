package hska.iwi.eShopMaster.oauth;

import java.util.ArrayList;

import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.RestTemplate;

public class OAuthConfig {
	private static final String ZUUL = "http://zuul:8770";
	private static final String TOKEN_URI = ZUUL + "/auth/oauth/token";
	private static final String CLIENT_ID = "client";
	private static final String CLIENT_SECRET = "clientsecret";
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
