package thiagodnf.nautilus.web.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import thiagodnf.nautilus.web.annotation.ContentType;
import thiagodnf.nautilus.web.annotation.NotEmptyFile;

public class UploadRealParetoFront {
	
	@NotEmptyFile
	@ContentType("text/plain")
	private MultipartFile file;
	
	@NotNull
	@NotEmpty
	private String name;
	
	@NotNull
	@NotEmpty
	private String problemId;

	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
