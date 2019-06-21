package thiagodnf.nautilus.web.config;

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

import thiagodnf.nautilus.web.service.UserDetailsService;
import thiagodnf.nautilus.web.util.Privileges;

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
                .antMatchers("/", "/images/**", "/css/**", "/js/**", "/webjars/**", "/websocket-server/**", "/api/**", "/signup/save", "/signup", "/user/confirmation").permitAll()
                
                .antMatchers(HttpMethod.GET, "/admin/**").hasAuthority(Privileges.SHOW_ADMIN_PAGE)
                
                .antMatchers(HttpMethod.GET, "/admin/users").hasAuthority(Privileges.SHOW_USERS_PAGE)
                .antMatchers(HttpMethod.POST, "/user/update").hasAuthority(Privileges.SAVE_USER)
                .antMatchers(HttpMethod.POST, "/user/delete/**").hasAuthority(Privileges.DELETE_USER)
                .antMatchers(HttpMethod.GET, "/user/edit/**").hasAuthority(Privileges.EDIT_USER)
                
                .antMatchers(HttpMethod.GET, "/admin/roles").hasAuthority(Privileges.SHOW_ROLES_PAGE)
                .antMatchers(HttpMethod.GET, "/role/add").hasAuthority(Privileges.CREATE_ROLE)
                .antMatchers(HttpMethod.GET, "/role/edit/**").hasAuthority(Privileges.EDIT_ROLE)
                .antMatchers(HttpMethod.POST, "/role/save").hasAuthority(Privileges.SAVE_ROLE)
                .antMatchers(HttpMethod.POST, "/role/delete/**").hasAuthority(Privileges.DELETE_ROLE)
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
