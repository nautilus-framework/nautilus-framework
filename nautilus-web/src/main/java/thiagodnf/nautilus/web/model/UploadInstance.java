package thiagodnf.nautilus.web.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import thiagodnf.nautilus.web.annotation.ContentType;
import thiagodnf.nautilus.web.annotation.NotEmptyFile;

public class UploadInstance {
	
	@NotEmptyFile
	@ContentType("text/plain")
	private MultipartFile file;
	
	@NotNull
	@NotEmpty
	private String problemKey;
	
	public UploadInstance(String problemKey) {
		this.problemKey = problemKey;
	}
	
	public UploadInstance() {
		
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getProblemKey() {
		return problemKey;
	}

	public void setProblemKey(String problemKey) {
		this.problemKey = problemKey;
	}
}
