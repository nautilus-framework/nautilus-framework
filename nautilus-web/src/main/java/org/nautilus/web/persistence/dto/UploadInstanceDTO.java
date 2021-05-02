package org.nautilus.web.persistence.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.nautilus.web.annotation.ContentType;
import org.nautilus.web.annotation.NotEmptyFile;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadInstanceDTO {
	
	@NotBlank
	private String problemId;
	
	@NotNull
	@NotEmptyFile
	@ContentType("text/plain")
	private MultipartFile file;
}
