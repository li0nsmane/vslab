package hska.webshop.resourcestandalone.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends ResourceServerConfigurerAdapter {

	@Primary
	@Bean
	public RemoteTokenServices tokenService() {
		RemoteTokenServices tokenService = new RemoteTokenServices();
		tokenService.setCheckTokenEndpointUrl("http://localhost:8081/auth/oauth/check_token");
		tokenService.setClientId("client");
		tokenService.setClientSecret("clientsecret");
		return tokenService;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/books/public").permitAll().and().authorizeRequests()
				.antMatchers("/books/admin").hasRole("ADMIN").antMatchers("/books/**").hasRole("USER").anyRequest()
				.authenticated();
	}
}
