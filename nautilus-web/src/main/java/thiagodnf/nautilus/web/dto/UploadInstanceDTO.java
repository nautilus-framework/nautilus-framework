package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import thiagodnf.nautilus.web.annotation.ContentType;
import thiagodnf.nautilus.web.annotation.NotEmptyFile;

@Getter
@Setter
@AllArgsConstructor
public class UploadInstanceDTO {
	
	@NotBlank
	private String problemId;
	
	@NotNull
	@NotEmptyFile
	@ContentType("text/plain")
	private MultipartFile file;
}
