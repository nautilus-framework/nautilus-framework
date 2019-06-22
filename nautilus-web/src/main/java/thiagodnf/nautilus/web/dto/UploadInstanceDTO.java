package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thiagodnf.nautilus.web.annotation.ContentType;
import thiagodnf.nautilus.web.annotation.HTMLAutoFocus;
import thiagodnf.nautilus.web.annotation.HTMLRequired;
import thiagodnf.nautilus.web.annotation.NotEmptyFile;

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
