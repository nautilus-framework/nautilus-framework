package thiagodnf.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "exception.InstanceDataNotFoundException")
public class InstanceDataNotFoundException extends AbstractRedirectException {

	private static final long serialVersionUID = 3213718309967112653L;
	
}