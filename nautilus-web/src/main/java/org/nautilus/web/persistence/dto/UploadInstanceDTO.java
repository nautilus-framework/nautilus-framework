package org.nautilus.web.persistence.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.nautilus.web.annotation.ContentType;
import org.nautilus.web.annotation.HTMLAutoFocus;
import org.nautilus.web.annotation.HTMLRequired;
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
	@HTMLAutoFocus
	private String problemId;
	
	@NotNull
	@NotEmptyFile
	@ContentType("text/plain")
	@HTMLRequired
	private MultipartFile file;
}
