package thiagodnf.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "exception.FileAlreadyExistsException")
public class FileAlreadyExistsException extends AbstractRedirectException {

	private static final long serialVersionUID = 3213128309967170653L;
	
}
