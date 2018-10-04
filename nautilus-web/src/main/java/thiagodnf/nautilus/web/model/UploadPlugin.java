package thiagodnf.nautilus.web.model;

import org.springframework.web.multipart.MultipartFile;

import thiagodnf.nautilus.web.annotation.ContentType;
import thiagodnf.nautilus.web.annotation.NotEmptyFile;

public class UploadPlugin {
	
	@NotEmptyFile
	//@ContentType("application/json")
	private MultipartFile file;
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
