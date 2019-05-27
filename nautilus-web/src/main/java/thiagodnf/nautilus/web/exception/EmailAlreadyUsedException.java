package thiagodnf.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "exception.EmailAlreadyUsedException")
public class EmailAlreadyUsedException extends AbstractRedirectException {

	private static final long serialVersionUID = -1190473755353340161L;
}
