package thiagodnf.nautilus.web.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.util.Converter;

public class Privilege {

	
	@Id
	private String id;
	
	@NotNull
	@NotEmpty
	private String name;
	
	@NotNull
	@NotEmpty
	private String groupName;
	
	public Privilege() {
		this("Default");
	}

	public Privilege(String name) {
		this(name, "Ungrouped");
	}

	public Privilege(String name, String groupName) {

		Preconditions.checkNotNull(name, "The name should not be null");
		Preconditions.checkArgument(!name.isEmpty(), "The name should not be empty");
		Preconditions.checkNotNull(groupName, "The groupName should not be null");
		Preconditions.checkArgument(!groupName.isEmpty(), "The groupName should not be empty");

		this.name = name;
		this.groupName = groupName;
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String toString() {
		return Converter.toJson(this);
	}
}
