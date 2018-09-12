package thiagodnf.nautilus.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "exception.ExecutionNotFoundException")
public class ExecutionNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1137473755353340161L;
}
