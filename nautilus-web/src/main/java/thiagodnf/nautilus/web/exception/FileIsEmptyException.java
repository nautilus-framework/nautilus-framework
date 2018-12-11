package thiagodnf.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "exception.FileIsEmptyException")
public class FileIsEmptyException extends RedirectException {

	private static final long serialVersionUID = 3212128309967170653L;
	
}
