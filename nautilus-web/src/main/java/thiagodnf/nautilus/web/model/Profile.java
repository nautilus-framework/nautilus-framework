package thiagodnf.nautilus.web.model;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;

import thiagodnf.nautilus.core.util.Converter;

public class Profile {

	@Id
	private String id;
	
	@NotBlank
	private String firstname;

	@NotBlank
	private String lastname;
	
	private String biography;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String toString() {
		return Converter.toJson(this);
	}
}
