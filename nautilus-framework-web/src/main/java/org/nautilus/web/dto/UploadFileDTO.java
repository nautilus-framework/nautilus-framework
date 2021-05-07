package org.nautilus.web.dto;

import javax.validation.constraints.NotNull;

import org.nautilus.web.annotation.NotEmptyFile;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadFileDTO {
	
	@NotNull
	@NotEmptyFile
	private MultipartFile file;
}
