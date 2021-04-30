package org.nautilus.web.persistence.model;

import org.nautilus.web.annotation.ContentType;
import org.nautilus.web.annotation.NotEmptyFile;
import org.springframework.web.multipart.MultipartFile;

public class UploadPlugin {
	
	@NotEmptyFile
	@ContentType({"application/octet-stream", "application/java-archive"})
	private MultipartFile file;
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
