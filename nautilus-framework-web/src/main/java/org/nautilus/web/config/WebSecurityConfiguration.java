package org.nautilus.web.config;

import org.nautilus.web.service.UserDetailsService;
import org.nautilus.web.util.Privileges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/api/**", "/forms", "/images/**", "/css/**", "/js/**", "/webjars/**", "/ws/**", "/optimize/execution/cancel/**", "/signup/save", "/signup", "/user/confirmation").permitAll()
                
                .antMatchers(HttpMethod.GET, "/admin/**").hasAuthority(Privileges.IS_ADMIN)
                
                .antMatchers(HttpMethod.POST, "/user/update").hasAuthority(Privileges.IS_ADMIN)
                .antMatchers(HttpMethod.POST, "/user/delete/**").hasAuthority(Privileges.IS_ADMIN)
                .antMatchers(HttpMethod.GET, "/user/edit/**").hasAuthority(Privileges.IS_ADMIN)
                
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/login/success")
                .permitAll()
                .and()
	            .rememberMe()
                .and()
            .logout()
            	.invalidateHttpSession(true)
            	.permitAll();
    }
	
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
}
