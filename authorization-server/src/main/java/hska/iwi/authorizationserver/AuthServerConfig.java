package hska.iwi.authorizationserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@SuppressWarnings("deprecation")
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    AuthenticationManager authenticationManager;

/*
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
*/

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("messaging-client")
                .secret("secret")
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "refresh_token")
          //      .authorities("USER")
          //      .redirectUris("http://localhost:8080/client/authorized")
                .scopes("openid");
    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authorizationCodeServices(authorizationCodeServices())
// injects the Spring Security authentication manager (set up in WebSecurityConfiguration )
            .authenticationManager(authenticationManager)
            .tokenStore(tokenStore())
            .approvalStoreDisabled();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

// configure security for /oauth/check_token and /oauth/token_key endpoint
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
// creates authorization codes, stores the codes in memory.
        return new InMemoryAuthorizationCodeServices();
    }
}
