package thiagodnf.nautilus.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.util.Converter;

public class Role {
	
	public static final String ADMIN = "Admin";
	
	public static final String USER = "User";

	@Id
	private String id;
	
	@NotBlank
	private String name;
	
	@NotNull
	private List<String> privileges;
	
	private boolean isEditable;
	
	public Role() {
		this("");
	}
	
	public Role(String name) {
		this(name, new ArrayList<>(), true);
	}
	
	public Role(String name, List<String> privileges, boolean isEditable) {

		Preconditions.checkNotNull(name, "The name should not be null");
		Preconditions.checkNotNull(privileges, "The privileges should not be null");
		
		this.name = name;
		this.privileges = privileges;
		this.isEditable = isEditable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}
	
	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public String toString() {
		return Converter.toJson(this);
	}
}
