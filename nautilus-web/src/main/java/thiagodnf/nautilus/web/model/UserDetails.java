package thiagodnf.nautilus.web.model;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

public class UserDetails extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1816387556811377455L;

	private User user;

	public UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public UserDetails(User user, Set<GrantedAuthority> grantedAuthorities) {
		this(user.getEmail(), user.getPassword(), grantedAuthorities);
		this.user = user;
	}

	public String getId() {
		return getUser().getId();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
