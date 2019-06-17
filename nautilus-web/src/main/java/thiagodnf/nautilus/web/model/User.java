package thiagodnf.nautilus.web.model;

import java.util.Date;

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
public class User {

	@Id
	private String id;
	
	private String email;

	private String password;
	
	private String roleId;
	
	private String firstname;

	private String lastname;
	
	private String confirmationToken;
	
	private boolean enabled = false;
	
	private boolean accountNonExpired = true;
	
	private boolean accountNonLocked = true;
	
	private boolean credentialsNonExpired = true;
	
	private boolean editable = true;
	
	private int decimalPlaces = 4;
	
	private String language = "en_US";

	@CreatedDate
	private Date creationDate;
	
	@LastModifiedDate
	private Date lastChangeDate;
}