package thiagodnf.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "exception.ExecutionNotFoundException")
public class ExecutionNotFoundException extends AbstractRedirectException {

	private static final long serialVersionUID = -1137473755353340161L;
}
