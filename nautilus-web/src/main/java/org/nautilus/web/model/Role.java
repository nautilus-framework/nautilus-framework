package org.nautilus.web.model;

import java.util.Date;
import java.util.List;

import org.nautilus.core.util.Converter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@NoArgsConstructor
public class Role {
	
	public static final String ADMIN = "Admin";
	
	public static final String USER = "User";

	@Id
	private String id;
	
	private String name;
	
	private List<String> privileges;
	
	private boolean isEditable = true;
	
	@CreatedDate
	private Date creationDate;
	
	@LastModifiedDate
	private Date lastChangeDate;
	
	public String toString() {
		return Converter.toJson(this);
	}
}
