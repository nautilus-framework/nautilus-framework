package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thiagodnf.nautilus.web.annotation.ContentType;
import thiagodnf.nautilus.web.annotation.NotEmptyFile;

@Getter
@Setter
@NoArgsConstructor
public class UploadExecutionDTO {
	
	@NotNull
	@NotEmptyFile
	@ContentType("application/json")
	private MultipartFile file;
}
