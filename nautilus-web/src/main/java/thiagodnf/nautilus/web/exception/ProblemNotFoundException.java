package thiagodnf.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "exception.ProblemNotFoundException")
public class ProblemNotFoundException extends RedirectException {

	private static final long serialVersionUID = 3213718309967170653L;
	
}