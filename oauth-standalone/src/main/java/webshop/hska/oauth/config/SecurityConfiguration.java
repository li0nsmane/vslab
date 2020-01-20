package webshop.hska.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

//	@Bean
//	public UserDetailsService userDetailsService() {
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		manager.createUser(User.withUsername("user").password(passwordEncoder().encode("userPass")).roles("USER").build());
//		manager.createUser(User.withUsername("admin").password(passwordEncoder().encode("adminPass")).roles("ADMIN").build());
//		return manager;
//	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password(passwordEncoder().encode("secret")).roles("USER");
		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("secret")).roles("ADMIN");
		auth.inMemoryAuthentication().withUser("bob").password(passwordEncoder().encode("secret")).roles("BOB");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access", "/webjars/**").and()
				.formLogin().loginPage("/login").permitAll().failureUrl("/login?error").and().authorizeRequests()
				.anyRequest().authenticated();
	}

}
